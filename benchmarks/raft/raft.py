from .. import benchmark
from .. import cluster
from .. import host
from .. import parser_util
from .. import pd_util
from .. import perf_util
from .. import proc
from .. import prometheus
from .. import proto_util
from .. import read_write_workload
from .. import util
from typing import Any, Callable, Collection, Dict, List, NamedTuple, Optional
import argparse
import csv
import datetime
import enum
import enum
import itertools
import os
import pandas as pd
import paramiko
import subprocess
import time
import tqdm
import yaml


class ClientOptions(NamedTuple):
    resend_timeout: datetime.timedelta = datetime.timedelta(milliseconds=100)


class ElectionOptions(NamedTuple):
    ping_period: datetime.timedelta = datetime.timedelta(seconds=5)
    no_ping_timeout_min: datetime.timedelta = datetime.timedelta(seconds=10)
    no_ping_timeout_max: datetime.timedelta = datetime.timedelta(seconds=12)
    not_enough_votes_timeout_min: datetime.timedelta = datetime.timedelta(seconds=10)
    not_enough_votes_timeout_max: datetime.timedelta = datetime.timedelta(seconds=12)


class ParticipantOptions(NamedTuple):
    election_options: ElectionOptions = ElectionOptions()


class Input(NamedTuple):
    # System-wide parameters. ##################################################
    f: int
    num_client_procs: int
    num_warmup_clients_per_proc: int
    num_clients_per_proc: int

    # Benchmark parameters. ####################################################
    measurement_group_size: int
    warmup_duration: datetime.timedelta
    warmup_timeout: datetime.timedelta
    warmup_sleep: datetime.timedelta
    duration: datetime.timedelta
    timeout: datetime.timedelta
    client_lag: datetime.timedelta
    state_machine: str
    predetermined_read_fraction: int
    workload_label: str
    workload: read_write_workload.ReadWriteWorkload
    read_workload: read_write_workload.ReadWriteWorkload
    write_workload: read_write_workload.ReadWriteWorkload
    profiled: bool
    monitored: bool
    prometheus_scrape_interval: datetime.timedelta

    # Client parameters. #######################################################
    client_options: ClientOptions
    client_log_level: str

    # Leader options. ##########################################################
    participant_options: ElectionOptions
    participant_log_level: str


class RaftOutput(NamedTuple):
    read_output: benchmark.RecorderOutput
    write_output: benchmark.RecorderOutput


Output = RaftOutput


# Network ######################################################################
class RaftNet:
    def __init__(self, cluster: cluster.Cluster, input: Input) -> None:
        self._cluster = cluster.f(input.f)
        self._input = input

    class Placement(NamedTuple):
        clients: List[host.Endpoint]
        participants: List[host.Endpoint]

    def placement(self) -> Placement:
        ports = itertools.count(10000, 100)

        def portify(hosts: List[host.Host]) -> List[host.Endpoint]:
            return [host.Endpoint(h, next(ports)) for h in hosts]

        def cycle_take_n(n: int, hosts: List[host.Host]) -> List[host.Host]:
            return list(itertools.islice(itertools.cycle(hosts), n))

        n = 2 * self._input.f + 1

        return self.Placement(
            clients=portify(
                cycle_take_n(self._input.num_client_procs, self._cluster["clients"])
            ),
            participants=portify(cycle_take_n(n, self._cluster["participants"])),
        )

    def config(self) -> proto_util.Message:
        return {
            # "f": self._input.f,
            "participant_address": [
                {"host": e.host.ip(), "port": e.port}
                for e in self.placement().participants
            ],
        }


# Suite ########################################################################
class RaftSuite(benchmark.Suite[Input, Output]):
    def __init__(self) -> None:
        super().__init__()
        self._cluster = cluster.Cluster.from_json_file(
            self.args()["cluster"], self._connect
        )

    def _connect(self, address: str) -> host.Host:
        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.client.AutoAddPolicy)
        if self.args()['identity_file']:
            client.connect(address, key_filename=self.args()['identity_file'])
        else:
            client.connect(address)
        return host.RemoteHost(client)

    def run_benchmark(self,
                      bench: benchmark.BenchmarkDirectory,
                      args: Dict[Any, Any],
                      input: Input) -> Output:
        def java(heap_size: str) -> List[str]:
            cmd = ['java', f'-Xms{heap_size}', f'-Xmx{heap_size}']
            if input.monitored:
                cmd += [
                    # '-verbose:gc',
                    # '-XX:-PrintGC',
                    # '-XX:+PrintHeapAtGC',
                    # '-XX:+PrintGCDetails',
                    # '-XX:+PrintGCTimeStamps',
                    # '-XX:+PrintGCDateStamps',
                ]
            return cmd

        # Write config file.
        net = RaftNet(self._cluster, input)
        config = net.config()
        config_filename = bench.abspath('config.pbtxt')
        bench.write_string(config_filename,
                           proto_util.message_to_pbtext(config))
        bench.log('Config file config.pbtxt written.')

        # Launch participants.
        participant_procs: List[proc.Proc] = []
        for (i, participant) in enumerate(net.placement().participants):
            p = bench.popen(
                host=participant.host,
                label=f'participant_{i}',
                cmd=java("100m") + [
                    '-cp',
                    os.path.abspath(args['jar']),
                    'frankenpaxos.raft.ParticipantMain',
                    '--index',
                    str(i),
                    '--config',
                    config_filename,
                    '--log_level',
                    input.participant_log_level,
                    '--prometheus_host',
                    participant.host.ip(),
                    '--prometheus_port',
                    str(participant.port + 1) if input.monitored else '-1',
                    '--options.election.pingPeriod',
                    '{}s'.format(input.participant_options.
                                 ping_period.total_seconds()),
                    '--options.election.noPingTimeoutMin',
                    '{}s'.format(input.participant_options.
                                 no_ping_timeout_min.total_seconds()),
                    '--options.election.noPingTimeoutMax',
                    '{}s'.format(input.participant_options.
                                 no_ping_timeout_max.total_seconds()),
                    '--options.election.notEnoughVotesTimeoutMin',
                    '{}s'.format(input.participant_options.
                                 not_enough_votes_timeout_min.total_seconds()),
                    '--options.election.notEnoughVotesTimeoutMax',
                    '{}s'.format(input.participant_options.
                                 not_enough_votes_timeout_max.total_seconds()),
                ],
            )
            if input.profiled:
                p = perf_util.JavaPerfProc(bench, participant.host, p, f'participant_{i}')
            participant_procs.append(p)
        bench.log('Participants started.')

        # Launch Prometheus.
        if input.monitored:
            prometheus_config = prometheus.prometheus_config(
                int(input.prometheus_scrape_interval.total_seconds() * 1000), {
                    'raft_client': [
                        f'{e.host.ip()}:{e.port+1}'
                        for e in net.placement().clients
                    ],
                    'raft_participant': [
                        f'{e.host.ip()}:{e.port+1}'
                        for e in net.placement().participants
                    ],
                })
            bench.write_string('prometheus.yml', yaml.dump(prometheus_config))
            prometheus_server = bench.popen(
                host=net.placement().clients[0].host,
                label='prometheus',
                cmd=[
                    'prometheus',
                    f'--config.file={bench.abspath("prometheus.yml")}',
                    f'--storage.tsdb.path={bench.abspath("prometheus_data")}',
                ],
            )
            bench.log('Prometheus started.')

        # Lag clients.
        time.sleep(input.client_lag.total_seconds())
        bench.log('Client lag ended.')

        # Launch clients.
        workload_filename = bench.abspath('workload.pbtxt')
        bench.write_string(
            workload_filename,
            proto_util.message_to_pbtext(input.workload.to_proto()))
        read_workload_filename = bench.abspath('read_workload.pbtxt')
        bench.write_string(
            read_workload_filename,
            proto_util.message_to_pbtext(input.read_workload.to_proto()))
        write_workload_filename = bench.abspath('write_workload.pbtxt')
        bench.write_string(
            write_workload_filename,
            proto_util.message_to_pbtext(input.write_workload.to_proto()))

        client_procs: List[proc.Proc] = []
        for (i, client) in enumerate(net.placement().clients):
            p = bench.popen(
                host=client.host,
                label=f'client_{i}',
                # TODO(mwhittaker): For now, we don't run clients with large
                # heaps and verbose garbage collection because they are all
                # colocated on one machine.
                cmd=java("100m") + [
                    '-cp',
                    os.path.abspath(args['jar']),
                    'frankenpaxos.raft.ClientMain',
                    '--host',
                    client.host.ip(),
                    '--port',
                    str(client.port),
                    '--config',
                    config_filename,
                    '--log_level',
                    input.client_log_level,
                    '--prometheus_host',
                    client.host.ip(),
                    '--prometheus_port',
                    str(client.port + 1) if input.monitored else '-1',
                    '--measurement_group_size',
                    f'{input.measurement_group_size}',
                    '--warmup_duration',
                    f'{input.warmup_duration.total_seconds()}s',
                    '--warmup_timeout',
                    f'{input.warmup_timeout.total_seconds()}s',
                    '--warmup_sleep',
                    f'{input.warmup_sleep.total_seconds()}s',
                    '--num_warmup_clients',
                    f'{input.num_warmup_clients_per_proc}',
                    '--duration',
                    f'{input.duration.total_seconds()}s',
                    '--timeout',
                    f'{input.timeout.total_seconds()}s',
                    '--num_clients',
                    f'{input.num_clients_per_proc}',
                    '--output_file_prefix',
                    bench.abspath(f'client_{i}'),
                    '--predetermined_read_fraction',
                    f'{input.predetermined_read_fraction}',
                    '--workload',
                    f'{workload_filename}',
                    '--read_workload',
                    f'{read_workload_filename}',
                    '--write_workload',
                    f'{write_workload_filename}',
                ])
            if input.profiled:
                p = perf_util.JavaPerfProc(bench, client.host, p, f'client_{i}')
            client_procs.append(p)
        bench.log(f'Clients started and running for {input.duration}.')

        # Wait for clients to finish and then terminate participants.
        for p in client_procs:
            p.wait()
        for p in participant_procs:
            p.kill()
        if input.monitored:
            prometheus_server.kill()
        bench.log('Clients finished and processes terminated.')

        # Client i writes results to `client_i_data.csv`.
        client_csvs = [
            bench.abspath(f'client_{i}_data.csv')
            for i in range(input.num_client_procs)
        ]

        dummy_latency = benchmark.LatencyOutput(
            mean_ms = -1.0,
            median_ms = -1.0,
            min_ms = -1.0,
            max_ms = -1.0,
            p90_ms = -1.0,
            p95_ms = -1.0,
            p99_ms = -1.0,
        )
        dummy_throughput = benchmark.ThroughputOutput(
            mean = -1.0,
            median = -1.0,
            min = -1.0,
            max = -1.0,
            p90 = -1.0,
            p95 = -1.0,
            p99 = -1.0,
        )
        dummy_output = benchmark.RecorderOutput(
            latency = dummy_latency,
            start_throughput_1s = dummy_throughput,
        )

        labeled_data = benchmark.parse_labeled_recorder_data(
            bench,
            client_csvs,
            drop_prefix=datetime.timedelta(seconds=0),
            save_data=False)
        read_output = (labeled_data['read']
                       if 'read' in labeled_data
                       else dummy_output)
        write_output = (labeled_data['write']
                        if 'write' in labeled_data
                        else dummy_output)
        return RaftOutput(read_output = read_output,
                                write_output = write_output)

def get_parser() -> argparse.ArgumentParser:
    return parser_util.get_benchmark_parser()
