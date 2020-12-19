from .raftquorum import *


def main(args) -> None:
    class SmokeRaftQuorumSuite(RaftQuorumSuite):
        def args(self) -> Dict[Any, Any]:
            return vars(args)

        def inputs(self) -> Collection[Input]:
            return ([
                Input(
                    f = 0,
                    m = m,
                    n = n,
                    num_client_procs = num_client_procs,
                    measurement_group_size = 1,
                    warmup_duration = datetime.timedelta(seconds=0),
                    warmup_timeout = datetime.timedelta(seconds=0),
                    warmup_sleep = datetime.timedelta(seconds=0),
                    duration = datetime.timedelta(seconds=15),
                    timeout = datetime.timedelta(seconds=20),
                    client_lag = datetime.timedelta(seconds=5),
                    quorum_system = 'GRID',
                    state_machine = 'KeyValueStore',
                    workload_label = "cs262a",
                    workload = workload,
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

                for (m, n, num_client_procs, workload) in [
                    # 2 x 2 grid
                    (2, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (2, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (2, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (2, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (2, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 5 x 1 grid
                    (5, 1, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (5, 1, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (5, 1, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (5, 1, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (5, 1, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 1 x 5 grid
                    (1, 5, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (1, 5, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (1, 5, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (1, 5, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (1, 5, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 3 x 3 grid
                    (3, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (3, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (3, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (3, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (3, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 3 x 2 grid
                    (3, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (3, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (3, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (3, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (3, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 2 x 3 grid
                    (2, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (2, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (2, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (2, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (2, 3, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 4 x 2 grid
                    (4, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (4, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (4, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (4, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (4, 2, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),

                    # 2 x 4 grid
                    (2, 4, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0, write_size_mean=1, write_size_std=0)),
                    (2, 4, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.25, write_size_mean=1, write_size_std=0)),
                    (2, 4, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.5, write_size_mean=1, write_size_std=0)),
                    (2, 4, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=0.75, write_size_mean=1, write_size_std=0)),
                    (2, 4, 10, read_write_workload.UniformReadWriteWorkload(num_keys=1, read_fraction=1, write_size_mean=1, write_size_std=0)),
                ]
            ] * 5)[:]

        def summary(self, input: Input, output: Output) -> str:
            return str({
                'num_participants': 2 * input.f + 1,
                'num_client_procs': input.num_client_procs,
                'workload': input.workload,
                'write.latency.median_ms': \
                    f'{output.write_output.latency.median_ms:.6}',
                'write.start_throughput_1s.p90': \
                    f'{output.write_output.start_throughput_1s.p90:.6}',
                'read.latency.median_ms': \
                    f'{output.read_output.latency.median_ms:.6}',
                'read.start_throughput_1s.p90': \
                    f'{output.read_output.start_throughput_1s.p90:.8}',
            })

    suite = SmokeRaftQuorumSuite()
    with benchmark.SuiteDirectory(args.suite_directory,
                                  'raftquorum_smoke') as dir:
        suite.run_suite(dir)


if __name__ == '__main__':
    main(get_parser().parse_args())
