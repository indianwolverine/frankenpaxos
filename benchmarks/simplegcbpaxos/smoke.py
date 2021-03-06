from .simplegcbpaxos import *


def main(args) -> None:
    class SmokeSimpleGcBPaxosSuite(SimpleGcBPaxosSuite):
        def args(self) -> Dict[Any, Any]:
            return vars(args)

        def inputs(self) -> Collection[Input]:
            return [
                Input(
                    f = 1,
                    num_client_procs = 1,
                    num_warmup_clients_per_proc = 1,
                    num_clients_per_proc = 1,
                    num_leaders = 2,
                    jvm_heap_size = '100m',
                    warmup_duration = datetime.timedelta(seconds=2),
                    warmup_timeout = datetime.timedelta(seconds=3),
                    warmup_sleep = datetime.timedelta(seconds=0),
                    duration = datetime.timedelta(seconds=2),
                    timeout = datetime.timedelta(seconds=3),
                    client_lag = datetime.timedelta(seconds=0),
                    state_machine = 'Noop',
                    workload = workload.StringWorkload(size_mean=1, size_std=0),
                    profiled = args.profile,
                    monitored = args.monitor,
                    prometheus_scrape_interval =
                        datetime.timedelta(milliseconds=200),
                    leader_options = LeaderOptions(
                        resend_dependency_requests_timer_period = \
                            datetime.timedelta(seconds=60)
                    ),
                    leader_log_level = args.log_level,
                    proposer_options = ProposerOptions(
                        resend_phase1as_timer_period = \
                            datetime.timedelta(seconds=60),
                        resend_phase2as_timer_period = \
                            datetime.timedelta(seconds=60),
                    ),
                    proposer_log_level = args.log_level,
                    dep_service_node_options = DepServiceNodeOptions(),
                    dep_service_node_log_level = args.log_level,
                    acceptor_options = AcceptorOptions(),
                    acceptor_log_level = args.log_level,
                    replica_options = ReplicaOptions(
                        recover_vertex_timer_min_period = \
                            datetime.timedelta(seconds=60),
                        recover_vertex_timer_max_period = \
                            datetime.timedelta(seconds=120),
                        execute_graph_batch_size = 1,
                        execute_graph_timer_period = \
                            datetime.timedelta(seconds=1)
                    ),
                    replica_zigzag_options = ZigzagOptions(
                        vertices_grow_size = 1000,
                        garbage_collect_every_n_commands = 1000,
                    ),
                    replica_log_level = args.log_level,
                    garbage_collector_options = GarbageCollectorOptions(),
                    garbage_collector_log_level = args.log_level,
                    client_options = ClientOptions(
                        repropose_period = datetime.timedelta(seconds=60),
                    ),
                    client_log_level = args.log_level,
                )
            ]

        def summary(self, input: Input, output: Output) -> str:
            return str({
                'f': input.f,
                'num_client_procs': input.num_client_procs,
                'num_clients_per_proc': input.num_clients_per_proc,
                'latency.median_ms': f'{output.latency.median_ms:.6}',
                'start_throughput_1s.p90': f'{output.start_throughput_1s.p90:.6}',
            })

    suite = SmokeSimpleGcBPaxosSuite()
    with benchmark.SuiteDirectory(args.suite_directory,
                                  'simplegcbpaxos_smoke') as dir:
        suite.run_suite(dir)


if __name__ == '__main__':
    main(get_parser().parse_args())
