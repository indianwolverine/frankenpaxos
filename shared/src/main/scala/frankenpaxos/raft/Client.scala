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
      case Request.WriteCmdResponse(r) => handleWriteCommandResponse(src, r)
      case Request.ReadCmdResponse(r)  => handleReadCommandResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty ClientInbound encountered.")
      }
    }
  }

  private def handleWriteCommandResponse(src: Transport#Address, cmdRes: WriteCommandResponse) {
    logger.info(s"Got WriteCommandResponse from ${src}" 
                + s"| Success: ${cmdRes.success}"
                + s"| LeaderIndex: ${cmdRes.leaderIndex}"
                + s"| Command: ${cmdRes.cmd}"
                + s"| LogIndex: ${cmdRes.index}")
            
    if (!cmdRes.success) {
        leaderIndex = cmdRes.leaderIndex
        logger.info(s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}.")
        writeCommand(cmdRes.cmd)
    } else {
      logger.info(s"Command succussfully replicated!")
    }
  }

  private def handleReadCommandResponse(src: Transport#Address, cmdRes: ReadCommandResponse) {
    logger.info(s"Got ReadCommandResponse from ${src}" 
                + s"| Success: ${cmdRes.success}"
                + s"| LeaderIndex: ${cmdRes.leaderIndex}"
                + s"| Command: ${cmdRes.cmd}"
                + s"| LogIndex: ${cmdRes.index}")
            
    if (!cmdRes.success) {
        leaderIndex = cmdRes.leaderIndex
        logger.info(s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}.")
        readCommand(cmdRes.index)
    } else {
      logger.info(s"Read command successfully!")
    }
  }

  private def writeCommandImpl(cmd: String): Unit = {
    raftParticipants(leaderIndex).send(ParticipantInbound().withWriteCmdRequest(WriteCommandRequest(cmd = cmd)))
  }

  private def readCommandImpl(index: Int): Unit = {
    raftParticipants(leaderIndex).send(ParticipantInbound().withReadCmdRequest(ReadCommandRequest(index = index)))
  }

  def writeCommand(cmd: String): Unit = {
    transport.executionContext().execute(() => writeCommandImpl(cmd))
  }

  def readCommand(index: Int): Unit = {
    transport.executionContext().execute(() => readCommandImpl(index))
  }
}
