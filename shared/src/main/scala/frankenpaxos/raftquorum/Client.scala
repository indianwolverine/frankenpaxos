package frankenpaxos.raftquorum

import scala.scalajs.js.annotation._
import frankenpaxos.raft.{Client, Config}
import frankenpaxos.{Logger, ProtoSerializer}

@JSExportAll
object QuorumClientInboundSerializer extends ProtoSerializer[QuorumClientInbound] {
  type A = QuorumClientInbound
  override def toBytes(x: A): Array[Byte] = super.toBytes(x)
  override def fromBytes(bytes: Array[Byte]): A = super.fromBytes(bytes)
  override def toPrettyString(x: A): String = super.toPrettyString(x)
}

@JSExportAll
object QuorumClient {
  val serializer = QuorumClientInboundSerializer
}

@JSExportAll
class QuorumClient[Transport <: frankenpaxos.Transport[Transport]](
    srcAddress: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport]
) extends Client(srcAddress, transport, logger, config) {
  override type InboundMessage = QuorumClientInbound
  // override def serializer = QuorumClient.serializer

  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    import QuorumClientInbound.Request
    inbound.request match {
      case Request.ClientQuorumQueryResponse(r) =>
        handleClientQuorumQueryResponse(src, r)
      case _ => super.receive(src, inbound)
    }
  }

  private def handleClientQuorumQueryResponse(
      src: Transport#Address,
      quorumQueryResponse: ClientQuorumQueryResponse
  ) {

  }

  override def readImpl() = {
    // raftParticipants(leaderIndex).send(
    //   QuorumParticipantInbound().withClientQuery(ClientQuery(index = index))
    // )
  }
}
