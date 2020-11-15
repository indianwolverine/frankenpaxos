package frankenpaxos.raftquorum

import scala.scalajs.js.annotation._
import frankenpaxos.raft.{Participant, Config, ElectionOptions}
import frankenpaxos.{Logger}


@JSExportAll
class QuorumParticipant[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport],
    // A potential initial leader. If participants are initialized with a
    // leader, at most one leader should be set.
    leader: Option[Transport#Address] = None,
    options: ElectionOptions = ElectionOptions.default
) extends Participant(address, transport, logger, config, leader, options) {

}