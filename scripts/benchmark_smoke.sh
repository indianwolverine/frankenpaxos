#! /usr/bin/env bash

set -euo pipefail

main() {
    for protocol in raft; do
                    # unreplicated batchedunreplicated multipaxos fasterpaxos \
                    # supermultipaxos vanillamencius mencius supermencius \
                    # fastmultipaxos epaxos simplebpaxos superbpaxos \
                    # simplegcbpaxos unanimousbpaxos matchmakermultipaxos \
                    # horizontal; do
        echo "Running $protocol."
        python3 -m "benchmarks.${protocol}.smoke" \
            -m \
            --cluster "benchmarks/${protocol}/local_cluster.json" \
            -i ~/.ssh/id_rsa
        echo
    done
}

main "$@"
