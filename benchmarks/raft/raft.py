from .. import workload
from .. import cluster
from ..workload import Workload
from typing import NamedTuple
import datetime


class ClientOptions(NamedTuple):
    resend_timeout: datetime.timedelta = datetime.timedelta(milliseconds=100)


class ElectionOptions(NamedTuple):
    ping_period: datetime.timedelta = datetime.timedelta(seconds=5)
    no_ping_timeout_min: datetime.timedelta = datetime.timedelta(seconds=10)
    no_ping_timeout_max: datetime.timedelta = datetime.timedelta(seconds=12)
    not_enough_votes_timeout_min: datetime.timedelta = datetime.timedelta(seconds=10)
    not_enough_votes_timeout_max: datetime.timedelta = datetime.timedelta(seconds=12)


class Input(NamedTuple):
    # System-wide parameters. ##################################################
    f: int
    num_client_procs: int
    num_warmup_clients_per_proc: int
    num_clients_per_proc: int

    # Benchmark parameters. ####################################################
    warmup_duration: datetime.timedelta
    warmup_timeout: datetime.timedelta
    warmup_sleep: datetime.timedelta
    duration: datetime.timedelta
    timeout: datetime.timedelta
    client_lag: datetime.timedelta
    state_machine: str
    workload: Workload
    profiled: bool
    monitored: bool
    prometheus_scrape_interval: datetime.timedelta

    # Client parameters. #######################################################
    client_options: ClientOptions
    client_log_level: str


class RaftOutput(NamedTuple):
    read_output: benchmark.RecorderOutput
    write_output: benchmark.RecorderOutput


Output = RaftOutput


# Network ######################################################################
class RaftNet:
    def __init__(self, cluster: cluster.Cluster, input: Input) -> None:
        self._cluster = cluster.f(input.f)
        self._input = input

    def _connect(self, address: str) -> host.Host:
        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.client.AutoAddPolicy)
        if self._key_filename:
            client.connect(address, key_filename=self._key_filename)
        else:
            client.connect(address)
        return host.RemoteHost(client)

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
            "f": self._input.f,
            "participant_address": [
                {"host": e.host.ip(), "port": e.port}
                for e in self.placement().participants
            ],
        }


# Suite ########################################################################
# TODO: write suite
class RaftSuite(benchmark.Suite[Input, Output]):
    pass


def get_parser() -> argparse.ArgumentParser:
    return parser_util.get_benchmark_parser()
