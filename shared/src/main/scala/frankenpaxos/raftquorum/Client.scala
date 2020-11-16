package frankenpaxos.raftquorum

import scala.scalajs.js.annotation._
import scala.concurrent.{Future, Promise}
import frankenpaxos.{Actor, Logger, ProtoSerializer}
import scala.util.Random

@JSExportAll
object QuorumClientInboundSerializer
    extends ProtoSerializer[QuorumClientInbound] {
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
) extends Actor(srcAddress, transport, logger) {
  override type InboundMessage = QuorumClientInbound
  override def serializer = QuorumClient.serializer

  // The set of raft nodes.
  val raftParticipants: Seq[Chan[QuorumParticipant[Transport]]] =
    for (participantAddress <- config.participantAddresses)
      yield chan[QuorumParticipant[Transport]](
        participantAddress,
        QuorumParticipant.serializer
      )

  // index of presumed leader
  var leaderIndex = 0

  // random
  val rand = new Random();

  // pending action - No need for lock since client acts synchronously
  @JSExportAll
  sealed trait PendingState

  @JSExportAll
  case class PendingWrite(
      promise: Promise[Boolean],
      cmd: String,
      resendTimer: Transport#Timer
  ) extends PendingState

  @JSExportAll
  case class PendingRead(
      promise: Promise[String],
      index: Integer,
      resendTimer: Transport#Timer
  ) extends PendingState

  var pending: Option[PendingState] = None

  logger.info(s"Raft Quorum client listening on $srcAddress.")

  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    import QuorumClientInbound.Request
    inbound.request match {
      case Request.ClientRequestResponse(r) =>
        handleClientRequestResponse(src, r)
      case Request.ClientQuorumQueryResponse(r) =>
        handleClientQuorumQueryResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty ClientInbound encountered.")
      }
    }
  }

  // Helpers

  private def makePendingWriteResendTimer(
      cmd: String
  ): Transport#Timer = {
    lazy val t: Transport#Timer = timer(
      s"resendPendingWrite",
      java.time.Duration.ofSeconds(10),
      () => {
        leaderIndex = rand.nextInt(raftParticipants.size)
        writeImpl(cmd)
        t.start()
      }
    )
    t.start()
    t
  }

  private def makePendingReadResendTimer(
      index: Integer
  ): Transport#Timer = {
    lazy val t: Transport#Timer = timer(
      s"resendPendingWrite",
      java.time.Duration.ofSeconds(10),
      () => {
        leaderIndex = rand.nextInt(raftParticipants.size)
        readImpl(index)
        t.start()
      }
    )
    t.start()
    t
  }

  private def handleClientRequestResponse(
      src: Transport#Address,
      requestResponse: frankenpaxos.raft.ClientRequestResponse
  ) {
    logger.info(
      s"Got ClientRequestResponse from ${src}"
        + s"| Success: ${requestResponse.success}"
        + s"| Response: ${requestResponse.response}"
        + s"| Leader Hint: ${requestResponse.leaderHint}"
    )
    pending match {
      case Some(pendingWrite: PendingWrite) =>
        pendingWrite.resendTimer.stop()
        if (!requestResponse.success) {
          if (requestResponse.response == "NOT_LEADER") {
            leaderIndex = requestResponse.leaderHint
            logger.info(
              s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}."
            )
            writeImpl(pendingWrite.cmd)
          } else {
            logger.info(
              s"PendingWrite failed: ${pendingWrite}."
            )
            pendingWrite.promise.failure(new Exception("Write failed"))
          }
        } else {
          logger.info("Command successfully replicated!")
          pendingWrite.promise.success(true)
          pending = None
        }
      case Some(_: PendingRead) =>
        logger.error("Request response received while no pending read exists.")
      case None =>
        logger.error(
          "Request response received while no pending action exists."
        )
    }
  }

  private def handleClientQuorumQueryResponse(
      src: Transport#Address,
      quorumQueryResponse: ClientQuorumQueryResponse
  ) {}

  private def writeImpl(cmd: String): Unit = {
    raftParticipants(leaderIndex).send(
      QuorumParticipantInbound().withClientRequest(
        frankenpaxos.raft.ClientRequest(cmd = cmd)
      )
    )
  }

  private def readImpl(index: Int): Unit = {
    // raftParticipants(leaderIndex).send(
    //   QuorumParticipantInbound().withClientQuorumQuery(ClientQuorumQuery(index = index))
    // )
    
    // - send req to random majority of participants with queryIndex 0
    // - set state to pending read
    // - once majority has replied
    //   - get max last accepted
    //   - if commitindex >= last accepted for that participant
    //       - read is done, return command read
    //   - else 
    //       - send req with queryIndex = max last accepted to random participant until returned commit index >= queryIndex
  }

  // Interface

  def write(cmd: String): Future[Boolean] = {
    if (pending != None) {
      throw new Exception("An action is already pending!")
    }
    val promise = Promise[Boolean]()
    pending = Some(
      PendingWrite(
        promise = promise,
        cmd = cmd,
        resendTimer = makePendingWriteResendTimer(cmd)
      )
    )
    transport.executionContext().execute(() => writeImpl(cmd))
    promise.future
  }

  def read(index: Int): Future[String] = {
    if (pending != None) {
      throw new Exception("An action is already pending!")
    }
    val promise = Promise[String]()
    pending = Some(
      PendingRead(
        promise = promise,
        index = index,
        resendTimer = makePendingReadResendTimer(index)
      )
    )
    transport.executionContext().execute(() => readImpl(index))
    promise.future
  }
}
