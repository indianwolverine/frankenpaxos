package frankenpaxos.raft

import frankenpaxos.{Actor, Chan, Logger, ProtoSerializer}
import scala.concurrent.{Future, Promise}
import scala.scalajs.js.annotation._

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

  // pending action - No need for lock since client acts synchronously
  @JSExportAll
  sealed trait PendingState

  @JSExportAll
  case class PendingWrite(
      promise: Promise[Boolean],
      cmd: String
  ) extends PendingState

  @JSExportAll
  case class PendingRead(
      promise: Promise[String],
      index: Integer
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

  private def handleClientRequestResponse(
      src: Transport#Address,
      requestResponse: ClientRequestResponse
  ) {
    logger.info(
      s"Got ClientRequestResponse from ${src}"
        + s"| Success: ${requestResponse.success}"
        + s"| Response: ${requestResponse.response}"
        + s"| Leader Hint: ${requestResponse.leaderHint}"
    )
    pending match {
      case Some(pendingWrite: PendingWrite) =>
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

  private def handleClientQueryResponse(
      src: Transport#Address,
      queryResponse: ClientQueryResponse
  ) {
    logger.info(
      s"Got ClientQueryResponse from ${src}"
        + s"| Success: ${queryResponse.success}"
        + s"| Response: ${queryResponse.response}"
        + s"| Leader Hint: ${queryResponse.leaderHint}"
    )
    pending match {
      case Some(pendingWrite: PendingWrite) =>
        logger.error("Request response received while no pending write exists.")
      case Some(pendingRead: PendingRead) =>
        if (!queryResponse.success) {
          if (queryResponse.response == "NOT_LEADER") {
            leaderIndex = queryResponse.leaderHint
            logger.info(
              s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}."
            )
            readImpl(pendingRead.index)
          } else {
            logger.info(
              s"PendingRead failed: ${pendingRead}."
            )
            pendingRead.promise.failure(new Exception("Read failed"))
          }
        } else {
          logger.info("Command successfully replicated!")
          pendingRead.promise.success(queryResponse.response)
          pending = None
        }
      case None =>
        logger.error(
          "Request response received while no pending action exists."
        )
    }
  }

  private def writeImpl(cmd: String): Unit = {
    raftParticipants(leaderIndex).send(
      ParticipantInbound().withClientRequest(ClientRequest(cmd = cmd))
    )
  }

  private def readImpl(index: Int): Unit = {
    raftParticipants(leaderIndex).send(
      ParticipantInbound().withClientQuery(ClientQuery(index = index))
    )
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
        cmd = cmd
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
        index = index
      )
    )
    transport.executionContext().execute(() => readImpl(index))
    promise.future
  }
}
