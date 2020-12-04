package frankenpaxos.raft

import frankenpaxos.{Actor, Chan, Logger, ProtoSerializer}
import scala.concurrent.{Future, Promise}
import scala.scalajs.js.annotation._
import scala.util.Random
import com.google.protobuf.ByteString

import scala.concurrent.ExecutionContext.Implicits.global

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

// Key assumption - if client goes down it does not come back up. If this was
// possible sequence numbers and client sessions would need to be implemented.
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
      yield chan[Participant[Transport]](
        participantAddress,
        Participant.serializer
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
      promise: Promise[Array[Byte]],
      cmd: Array[Byte],
      resendTimer: Transport#Timer
  ) extends PendingState

  @JSExportAll
  case class PendingRead(
      promise: Promise[Array[Byte]],
      query: Array[Byte],
      resendTimer: Transport#Timer
  ) extends PendingState

  var pending: Option[PendingState] = None

  logger.info(s"Raft client listening on $srcAddress.")

  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    import ClientInbound.Request
    inbound.request match {
      case Request.ClientRequestResponse(r) =>
        handleClientRequestResponse(src, r)
      case Request.ClientQueryResponse(r) => handleClientQueryResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty ClientInbound encountered.")
      }
    }
  }

  // Helpers

  private def makePendingWriteResendTimer(
      cmd: Array[Byte]
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
      query: Array[Byte]
  ): Transport#Timer = {
    lazy val t: Transport#Timer = timer(
      s"resendPendingRead",
      java.time.Duration.ofSeconds(10),
      () => {
        leaderIndex = rand.nextInt(raftParticipants.size)
        readImpl(query)
        t.start()
      }
    )
    t.start()
    t
  }

  private def handleClientRequestResponse(
      src: Transport#Address,
      requestResponse: ClientRequestResponse
  ) {
    logger.info(
      s"Got ClientRequestResponse from ${src}"
        + s" | Success: ${requestResponse.success}"
        + s" | Response: ${requestResponse.response}"
        + s" | Leader Hint: ${requestResponse.leaderHint}"
    )
    pending match {
      case Some(pendingWrite: PendingWrite) =>
        pendingWrite.resendTimer.stop()
        if (!requestResponse.success) {
          if (new String(requestResponse.response.toByteArray()) == "NOT_LEADER") {
            leaderIndex = requestResponse.leaderHint
            logger.info(
              s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}."
            )
            writeImpl(pendingWrite.cmd)
          } else {
            logger.error(
              s"PendingWrite failed: ${pendingWrite}."
            )
            pendingWrite.promise.failure(new Exception("Write failed"))
          }
        } else {
          logger.info("Command successfully replicated!")
          pendingWrite.promise.success(requestResponse.response.toByteArray())
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

  private def handleClientQueryResponse(
      src: Transport#Address,
      queryResponse: ClientQueryResponse
  ) {
    logger.info(
      s"Got ClientQueryResponse from ${src}"
        + s" | Success: ${queryResponse.success}"
        + s" | Response: ${queryResponse.response}"
        + s" | Leader Hint: ${queryResponse.leaderHint}"
    )
    pending match {
      case Some(pendingWrite: PendingWrite) =>
        logger.error("Request response received while no pending write exists.")
      case Some(pendingRead: PendingRead) =>
        pendingRead.resendTimer.stop()
        if (!queryResponse.success) {
          if (new String(queryResponse.response.toByteArray()) == "NOT_LEADER") {
            leaderIndex = queryResponse.leaderHint
            logger.info(
              s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}."
            )
            readImpl(pendingRead.query)
          } else {
            logger.error(
              s"PendingRead failed: ${pendingRead}."
            )
            pendingRead.promise.failure(new Exception("Read failed"))
          }
        } else {
          logger.info("Read Command successfully replicated!")
          pendingRead.promise.success(queryResponse.response.toByteArray())
          pending = None
        }
      case None =>
        logger.error(
          "Request response received while no pending action exists."
        )
    }
  }

  private def writeImpl(cmd: Array[Byte]): Unit = {
    raftParticipants(leaderIndex).send(
      ParticipantInbound().withClientRequest(
        ClientRequest(
          CommandOrNoop().withCommand(Command(cmd = ByteString.copyFrom(cmd)))
        )
      )
    )
  }

  private def readImpl(query: Array[Byte]): Unit = {
    raftParticipants(leaderIndex).send(
      ParticipantInbound().withClientQuery(
        ClientQuery(ReadCommand(query = ByteString.copyFrom(query)))
      )
    )
  }

  // Interface

  def write(cmd: Array[Byte]): Future[Array[Byte]] = {
    if (pending != None) {
      throw new Exception("An action is already pending!")
    }
    val promise = Promise[Array[Byte]]()
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

  def read(query: Array[Byte]): Future[Array[Byte]] = {
    if (pending != None) {
      throw new Exception("An action is already pending!")
    }
    val promise = Promise[Array[Byte]]()
    pending = Some(
      PendingRead(
        promise = promise,
        query = query,
        resendTimer = makePendingReadResendTimer(query)
      )
    )
    transport.executionContext().execute(() => readImpl(query))
    promise.future
  }
}
