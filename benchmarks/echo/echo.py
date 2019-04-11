from .. import benchmark
from .. import parser_util
from .. import pd_util
from .. import prometheus
from mininet.net import Mininet
from typing import Callable, Dict, List, NamedTuple
import argparse
import csv
import enum
import mininet
import os
import pandas as pd
import subprocess
import time
import tqdm
import yaml


class Input(NamedTuple):
    net_name: str
    num_clients: int
    num_threads_per_client: int
    duration_seconds: float
    profiled: bool
    monitored: bool
    prometheus_scrape_interval_ms: int


class Output(NamedTuple):
    mean_latency_ms: float
    median_latency_ms: float
    p90_latency_ms: float
    p95_latency_ms: float
    p99_latency_ms: float

    mean_1_second_throughput: float
    median_1_second_throughput: float
    p90_1_second_throughput: float
    p95_1_second_throughput: float
    p99_1_second_throughput: float

    mean_2_second_throughput: float
    median_2_second_throughput: float
    p90_2_second_throughput: float
    p95_2_second_throughput: float
    p99_2_second_throughput: float

    mean_5_second_throughput: float
    median_5_second_throughput: float
    p90_5_second_throughput: float
    p95_5_second_throughput: float
    p99_5_second_throughput: float


class EchoNet(object):
    def __init__(self) -> None:
        pass

    def __enter__(self) -> 'EchoNet':
        self.net().start()
        return self

    def __exit__(self, cls, exn, traceback) -> None:
        self.net().stop()

    def net(self) -> Mininet:
        raise NotImplementedError()

    def clients(self) -> List[mininet.node.Node]:
        raise NotImplementedError()

    def server(self) -> mininet.node.Node:
        raise NotImplementedError()


class SingleSwitchNet(EchoNet):
    def __init__(self, num_clients: int) -> None:
        self._clients: List[mininet.node.Node] = []
        self._server: mininet.node.Node = None
        self._net = Mininet()

        switch = self._net.addSwitch('s1')
        self._net.addController('c')

        for i in range(num_clients):
            client = self._net.addHost(f'c{i}')
            self._net.addLink(client, switch)
            self._clients.append(client)

        self._server = self._net.addHost(f'h1')
        self._net.addLink(self._server, switch)

    def net(self) -> Mininet:
        return self._net

    def clients(self) -> List[mininet.node.Node]:
        return self._clients

    def server(self) -> mininet.node.Node:
        return self._server


def run_benchmark(bench: benchmark.BenchmarkDirectory,
                  args: argparse.Namespace,
                  input: Input,
                  net: EchoNet) -> Output:
    # Launch server.
    server_proc = bench.popen(
        f=net.server().popen,
        label='server',
        cmd = [
            'java',
            '-cp', os.path.abspath(args.jar),
            'frankenpaxos.echo.BenchmarkServerMain',
            '--host', net.server().IP(),
            '--port', '9000',
            '--prometheus_host', net.server().IP(),
            '--prometheus_port', '9001',
        ],
        profile=input.profiled
    )

    # Launch Prometheus, and give it some time to start.
    if input.monitored:
        prometheus_config = prometheus.prometheus_config(
            input.prometheus_scrape_interval_ms,
            {'echo_server': [f'{net.server().IP()}:9001']}
        )
        bench.write_string('prometheus.yml', yaml.dump(prometheus_config))
        prometheus_server = bench.popen(
            f=net.server().popen,
            label='prometheus',
            cmd = [
                'prometheus',
                f'--config.file={bench.abspath("prometheus.yml")}',
                f'--storage.tsdb.path={bench.abspath("prometheus_data")}',
            ],
        )
        time.sleep(3)

    # Launch clients.
    client_procs = []
    for (i, host) in enumerate(net.clients()):
        client_proc = bench.popen(
            f=net.server().popen,
            label=f'client_{i}',
            cmd = [
                'java',
                '-cp', os.path.abspath(args.jar),
                'frankenpaxos.echo.BenchmarkClientMain',
                '--server_host', net.server().IP(),
                '--server_port', '9000',
                '--host', host.IP(),
                '--port', '10000',
                '--duration', f'{input.duration_seconds}s',
                '--num_threads', str(input.num_threads_per_client),
                '--output_file_prefix', bench.abspath(f'client_{i}'),
            ]
        )
        client_procs.append(client_proc)

    # Wait for experiment to finish.
    for client_proc in client_procs:
        client_proc.wait()
    server_proc.terminate()
    if input.monitored:
        prometheus_server.terminate()

    # Every client thread j on client i writes results to `client_i_j.csv`. We
    # concatenate these results into a single CSV file.
    client_csvs = [bench.abspath(f'client_{i}_{j}.csv')
                   for i in range(input.num_clients)
                   for j in range(input.num_threads_per_client)]
    df = (pd_util.read_csvs(client_csvs, parse_dates=['start', 'stop'])
                 .set_index('start')
                 .sort_index(0))
    df.to_csv(bench.abspath('data.csv'))

    # Since we concatenate and save the file, we can throw away the originals.
    for client_csv in client_csvs:
        os.remove(client_csv)

    # We also compress the output data since it can get big.
    subprocess.call(['gzip', bench.abspath('data.csv')])

    # Next, we scrape data from Prometheus.
    if input.monitored:
        pq = prometheus.PrometheusQueryer(
            tsdb_path=bench.abspath('prometheus_data'),
            popen=lambda c: bench.popen(label='prometheus_querier', cmd=c)
        )
        p_df = pd.DataFrame()
        p_df[['echo_requests_total']] = pq.query('echo_requests_total[1y]')
        p_df.to_csv(bench.abspath('prometheus_data.csv'))

    latency_ms = df['latency_nanos'] / 1e6
    throughput_1s = pd_util.throughput(df, 1000)
    throughput_2s = pd_util.throughput(df, 2000)
    throughput_5s = pd_util.throughput(df, 5000)
    return Output(
        mean_latency_ms = latency_ms.mean(),
        median_latency_ms = latency_ms.median(),
        p90_latency_ms = latency_ms.quantile(.90),
        p95_latency_ms = latency_ms.quantile(.95),
        p99_latency_ms = latency_ms.quantile(.99),

        mean_1_second_throughput = throughput_1s.mean(),
        median_1_second_throughput = throughput_1s.median(),
        p90_1_second_throughput = throughput_1s.quantile(.90),
        p95_1_second_throughput = throughput_1s.quantile(.95),
        p99_1_second_throughput = throughput_1s.quantile(.99),

        mean_2_second_throughput = throughput_2s.mean(),
        median_2_second_throughput = throughput_2s.median(),
        p90_2_second_throughput = throughput_2s.quantile(.90),
        p95_2_second_throughput = throughput_2s.quantile(.95),
        p99_2_second_throughput = throughput_2s.quantile(.99),

        mean_5_second_throughput = throughput_5s.mean(),
        median_5_second_throughput = throughput_5s.median(),
        p90_5_second_throughput = throughput_5s.quantile(.90),
        p95_5_second_throughput = throughput_5s.quantile(.95),
        p99_5_second_throughput = throughput_5s.quantile(.99),
    )


def run_suite(args: argparse.Namespace,
              inputs: List[Input],
              make_net: Callable[[Input], EchoNet]) -> None:
    with benchmark.SuiteDirectory(args.suite_directory, 'echo') as suite:
        print(f'Running benchmark suite in {suite.path}.')
        suite.write_dict('args.json', vars(args))
        suite.write_string('inputs.txt', '\n'.join(str(i) for i in inputs))

        results_file = suite.create_file('results.csv')
        results_writer = csv.writer(results_file)
        results_writer.writerow(Input._fields + Output._fields)

        for input in tqdm.tqdm(inputs):
            with suite.benchmark_directory() as bench:
                with make_net(input) as net:
                    bench.write_string('input.txt', str(input))
                    bench.write_dict('input.json', input._asdict())
                    output = run_benchmark(bench, args, input, net)
                    row = [str(x) for x in list(input) + list(output)]
                    results_writer.writerow(row)
                    results_file.flush()


def _main(args) -> None:
    inputs = [
        Input(
            net_name='SingleSwitchNet',
            num_clients=num_clients,
            num_threads_per_client=1,
            duration_seconds=10,
            profiled=args.profile,
            monitored=args.monitor,
            prometheus_scrape_interval_ms=200,
        )
        for num_clients in [1, 2, 3]
    ] * 2

    def make_net(input) -> EchoNet:
        return SingleSwitchNet(num_clients=input.num_clients)

    run_suite(args, inputs, make_net)


def get_parser() -> argparse.ArgumentParser:
    return parser_util.get_benchmark_parser()


if __name__ == '__main__':
    _main(get_parser().parse_args())