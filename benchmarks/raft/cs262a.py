from .raft import *


def main(args) -> None:
    class SmokeRaftSuite(RaftSuite):
        def args(self) -> Dict[Any, Any]:
            return vars(args)

        def inputs(self) -> Collection[Input]:
            return ([
                Input(
                    f = f,
                    num_client_procs = num_client_procs,
                    num_warmup_clients_per_proc = 0,
                    num_clients_per_proc = num_clients_per_proc,
                    measurement_group_size = 1,
                    warmup_duration = datetime.timedelta(seconds=0),
                    warmup_timeout = datetime.timedelta(seconds=0),
                    warmup_sleep = datetime.timedelta(seconds=3),
                    duration = datetime.timedelta(seconds=15),
                    timeout = datetime.timedelta(seconds=20),
                    client_lag = datetime.timedelta(seconds=5),
                    state_machine = 'KeyValueStore',
                    predetermined_read_fraction = predetermined_read_fraction,
                    workload_label = "cs262a",
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
                    participant_options = ElectionOptions(
                        ping_period = datetime.timedelta(seconds=60),
                        no_ping_timeout_min = \
                            datetime.timedelta(seconds=120),
                        no_ping_timeout_max = \
                            datetime.timedelta(seconds=240),
                        not_enough_votes_timeout_min = \
                            datetime.timedelta(seconds=120),
                        not_enough_votes_timeout_max = \
                            datetime.timedelta(seconds=240),
                    ),
                    participant_log_level = args.log_level,
                    client_options = ClientOptions(),
                    client_log_level = args.log_level,
                )

                for (f, num_client_procs, num_clients_per_proc, 
                    predetermined_read_fraction) in [
                    # 3 participants
                    (1, 10, 1, 0),
                    (1, 10, 1, 25),
                    (1, 10, 1, 50),
                    (1, 10, 1, 70),

                    # 5 participants
                    (2, 10, 1, 0),
                    (2, 10, 1, 25),
                    (2, 10, 1, 50),
                    (2, 10, 1, 70),

                    # 7 participants
                    (3, 10, 1, 0),
                    (3, 10, 1, 25),
                    (3, 10, 1, 50),
                    (3, 10, 1, 70),
                ]
            ] * 1)[:]

        def summary(self, input: Input, output: Output) -> str:
            return str({
                'f': input.f,
                'num_participants': 2 * input.f + 1,
                'num_client_procs': input.num_client_procs,
                'num_clients_per_proc': input.num_clients_per_proc,
                'predetermined_read_fraction': input.predetermined_read_fraction,
                'write.latency.median_ms': \
                    f'{output.write_output.latency.median_ms:.6}',
                'write.start_throughput_1s.p90': \
                    f'{output.write_output.start_throughput_1s.p90:.6}',
                'read.latency.median_ms': \
                    f'{output.read_output.latency.median_ms:.6}',
                'read.start_throughput_1s.p90': \
                    f'{output.read_output.start_throughput_1s.p90:.8}',
                })

    suite = SmokeRaftSuite()
    with benchmark.SuiteDirectory(args.suite_directory,
                                  'raft_smoke') as dir:
        suite.run_suite(dir)


if __name__ == '__main__':
    main(get_parser().parse_args())
