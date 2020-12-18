from .raftquorum import *


def main(args) -> None:
    class SmokeRaftQuorumSuite(RaftQuorumSuite):
        def args(self) -> Dict[Any, Any]:
            return vars(args)

        def inputs(self) -> Collection[Input]:
            return [
                Input(
                    f = 1,
                    m = 0, 
                    n = 0,
                    num_client_procs = 1,
                    measurement_group_size = 1,
                    warmup_duration = datetime.timedelta(seconds=2),
                    warmup_timeout = datetime.timedelta(seconds=10),
                    warmup_sleep = datetime.timedelta(seconds=0),
                    duration = datetime.timedelta(seconds=2),
                    timeout = datetime.timedelta(seconds=3),
                    client_lag = datetime.timedelta(seconds=3),
                    quorum_system = 'MAJORITY',
                    state_machine = 'KeyValueStore',
                    workload_label = "smoke",
                    workload = read_write_workload.UniformReadWriteWorkload(
                        num_keys=1, read_fraction=0.5, write_size_mean=1,
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
