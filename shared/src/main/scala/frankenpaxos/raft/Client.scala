package frankenpaxos.raft

import frankenpaxos.Actor
import frankenpaxos.Logger
import frankenpaxos.ProtoSerializer
import frankenpaxos.Chan
import scala.scalajs.js.annotation._

@JSExportAll
object ClientInboundSerializer extends ProtoSerializer[ClientInbound] {
  type A = ClientInbound
  override def toBytes(x: A): Array[Byte] = super.toBytes(x)
  override def fromBytes(bytes: Array[Byte]): A = super.fromBytes(bytes)
  override def toPrettyString(x: A): String = super.toPrettyString(x)
}

@JSExportAll
object Client {
  val serializer = ClientInboundSerializer
}

@JSExportAll
class Client[Transport <: frankenpaxos.Transport[Transport]](
    srcAddress: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport]
) extends Actor(srcAddress, transport, logger) {
  override type InboundMessage = ClientInbound
  override def serializer = Client.serializer

  // The set of raft nodes.
  val raftParticipants: Seq[Chan[Participant[Transport]]] =
    for (participantAddress <- config.participantAddresses)
      yield
        chan[Participant[Transport]](
          participantAddress,
          Participant.serializer
        )

  // index of presumed leader
  var leaderIndex = 0

  logger.info(s"Raft client listening on $srcAddress.")

  override def receive(src: Transport#Address, inbound: InboundMessage): Unit = {
    import ClientInbound.Request
    inbound.request match {
      case Request.CmdResponse(r) => handleCommandResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty ClientInbound encountered.")
      }
    }
  }

  private def handleCommandResponse(src: Transport#Address, cmdRes: CommandResponse) {
    if (!cmdRes.success) {
        leaderIndex = cmdRes.leaderIndex
        logger.info(s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}.")
        sendCommand(cmdRes.cmd)
    }
  }

  private def sendCommandImpl(cmd: String): Unit = {
    raftParticipants(leaderIndex).send(ParticipantInbound().withCmdRequest(CommandRequest(cmd = cmd)))
  }

  def sendCommand(cmd: String): Unit = {
    transport.executionContext().execute(() => sendCommandImpl(cmd))
  }
}
