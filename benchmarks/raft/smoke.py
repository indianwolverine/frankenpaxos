from .raft import *


def main(args) -> None:
    class SmokeRaftSuite(RaftSuite):
        def args(self) -> Dict[Any, Any]:
            return vars(args)

        def inputs(self) -> Collection[Input]:
            return [
                Input(
                    f = 2,
                    num_client_procs = 3,
                    num_warmup_clients_per_proc = 3,
                    num_clients_per_proc = 3,
                    measurement_group_size = 1,
                    warmup_duration = datetime.timedelta(seconds=2),
                    warmup_timeout = datetime.timedelta(seconds=3),
                    warmup_sleep = datetime.timedelta(seconds=0),
                    duration = datetime.timedelta(seconds=2),
                    timeout = datetime.timedelta(seconds=3),
                    client_lag = datetime.timedelta(seconds=3),
                    state_machine = 'KeyValueStore',
                    predetermined_read_fraction = -1,
                    workload_label = "smoke",
                    workload = read_write_workload.UniformReadWriteWorkload(
                        num_keys=1, read_fraction=0.5, write_size_mean=1,
                        write_size_std=0),
                    read_workload = read_write_workload.UniformReadWriteWorkload(
                        num_keys=1, read_fraction=1.0, write_size_mean=1,
                        write_size_std=0),
                    write_workload = read_write_workload.UniformReadWriteWorkload(
                        num_keys=1, read_fraction=0.0, write_size_mean=1,
                        write_size_std=0),
                    profiled = args.profile,
                    monitored = args.monitor,
                    prometheus_scrape_interval =
                        datetime.timedelta(milliseconds=200),
                    participant_options = ElectionOptions(),
                    participant_log_level = args.log_level,
                    client_options = ClientOptions(),
                    client_log_level = args.log_level,
                )
            ]

        def summary(self, input: Input, output: Output) -> str:
            return str({
                'f': input.f,
                'num_participants': 2 * input.f + 1,
                'num_client_procs': input.num_client_procs,
                'num_clients_per_proc': input.num_clients_per_proc,
                'write.latency.median_ms': \
                    f'{output.write_output.latency.median_ms:.6}',
                'write.start_throughput_1s.p90': \
                    f'{output.write_output.start_throughput_1s.p90:.6}',
            })

    suite = SmokeRaftSuite()
    with benchmark.SuiteDirectory(args.suite_directory,
                                  'raft_smoke') as dir:
        suite.run_suite(dir)


if __name__ == '__main__':
    main(get_parser().parse_args())
