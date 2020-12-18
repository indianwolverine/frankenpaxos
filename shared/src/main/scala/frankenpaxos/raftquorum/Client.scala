package frankenpaxos.raftquorum

import scala.scalajs.js.annotation._
import scala.concurrent.{Future, Promise}
import frankenpaxos.{Actor, Logger, ProtoSerializer}
import frankenpaxos.quorums.QuorumSystem
import scala.util.Random
import com.google.protobuf.ByteString
import scala.collection.mutable.Map

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
    config: Config[Transport],
    val quorumSystem: QuorumSystem[Int]
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
      promise: Promise[Array[Byte]],
      cmd: Array[Byte],
      resendTimer: Transport#Timer
  ) extends PendingState

  @JSExportAll
  case class PendingRead(
      promise: Promise[Array[Byte]],
      query: Array[Byte],
      resendTimer: Transport#Timer,
      resendRinseTimer: Transport#Timer,
      var latestIndex: Int,
      var latestCommitted: Int,
      var response: Array[Byte],
      var quorumResponses: Set[Int]
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
        readImpl(query)
        t.start()
      }
    )
    t.start()
    t
  }

  private def makeResendRinseTimer(
      query: Array[Byte]
  ): Transport#Timer = {
    lazy val t: Transport#Timer = timer(
      s"rinse",
      java.time.Duration.ofSeconds(10),
      () => {
        rinse(query)
        t.start()
      }
    )
    t
  }

  private def rinse(
      query: Array[Byte]
  ): Unit = {
    logger.info("Sending rinse...")
    raftParticipants(rand.nextInt(raftParticipants.size)).send(
      QuorumParticipantInbound().withClientQuorumQuery(
        ClientQuorumQuery(
          frankenpaxos.raft.ReadCommand(query = ByteString.copyFrom(query))
        )
      )
    )
  }

  private def handleClientRequestResponse(
      src: Transport#Address,
      requestResponse: frankenpaxos.raft.ClientRequestResponse
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
            pending = None
            pendingWrite.promise.failure(new Exception("Write failed"))
          }
        } else {
          val response = requestResponse.response.toByteArray()
          logger.info(s"Write output received: ${response}")
          pending = None
          pendingWrite.promise.success(response)
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
  ) {
    logger.info(
      s"Got ClientQuorumQueryResponse from ${src}"
        + s" | Success: ${quorumQueryResponse.success}"
        + s" | Response ${quorumQueryResponse.response}"
        + s" | Latest Index: ${quorumQueryResponse.latestIndex}"
        + s" | Latest Committed: ${quorumQueryResponse.latestCommitted}"
    )
    pending match {
      case Some(_: PendingWrite) =>
        logger.error("Request response received while no pending write exists.")
      case Some(pendingRead: PendingRead) =>
        if (quorumQueryResponse.success) {
          pendingRead.resendTimer.reset()
          if (quorumSystem.isReadQuorum(pendingRead.quorumResponses)) {
            // Quorum has already been reached, this must be a rinse response
            logger.info("Got rinse response.")
            pendingRead.resendRinseTimer.reset()
            if (
              quorumQueryResponse.latestCommitted >= pendingRead.latestIndex
            ) {
              logger.info("Entry got committed! Stopping rinses.")
              pendingRead.resendTimer.stop()
              pendingRead.resendRinseTimer.stop()
              pending = None
              pendingRead.promise.success(pendingRead.response)
            } else {
              rinse(pendingRead.query)
            }
          } else {
            pendingRead.quorumResponses += config.participantAddresses.indexOf(
              src
            )
            if (quorumQueryResponse.latestIndex >= pendingRead.latestIndex) {
              pendingRead.latestIndex = quorumQueryResponse.latestIndex
              pendingRead.latestCommitted = pendingRead.latestCommitted.max(
                quorumQueryResponse.latestCommitted
              )
              pendingRead.response = quorumQueryResponse.response.toByteArray()
            }
            // If Quorum has been reached for the first time
            if (quorumSystem.isReadQuorum(pendingRead.quorumResponses)) {
              logger.info(s"Quorum reached: latestIndex = ${pendingRead.latestIndex}"
                + s" | latestCommitted = ${pendingRead.latestCommitted}")
              if (
                pendingRead.latestCommitted >= pendingRead.latestIndex
              ) {
                // Already committed, no need to rinse
                logger.info(s"No need to rinse, already committed, output: ${pendingRead.response}")
                pendingRead.resendTimer.stop()
                pendingRead.resendRinseTimer.stop()
                pending = None
                pendingRead.promise.success(pendingRead.response)
              } else {
                // Need to rinse for the first time
                logger.info("Starting rinsing...")
                rinse(pendingRead.query)
              }
            }
          }
        } else {
          logger.error("Failed ClientQuorumQueryResponse received.")
          pending = None
          pendingRead.promise.failure(new Exception("Read failed"))
        }
      case None =>
        logger.error(
          "Request response received while no pending action exists."
        )
    }
  }

  private def writeImpl(cmd: Array[Byte]): Unit = {
    raftParticipants(leaderIndex).send(
      QuorumParticipantInbound().withClientRequest(
        frankenpaxos.raft.ClientRequest(
          frankenpaxos.raft
            .CommandOrNoop()
            .withCommand(
              frankenpaxos.raft.Command(cmd = ByteString.copyFrom(cmd))
            )
        )
      )
    )
  }

  private def readImpl(query: Array[Byte]): Unit = {
    for (i <- quorumSystem.randomReadQuorum()) {
      raftParticipants(i).send(
        QuorumParticipantInbound().withClientQuorumQuery(
          ClientQuorumQuery(
            frankenpaxos.raft.ReadCommand(query = ByteString.copyFrom(query))
          )
        )
      )
    }
  }

  // Interface

  def write(cmd: Array[Byte]): Future[Array[Byte]] = {
    logger.info("Performing write...")
    if (pending != None) {
      logger.error(s"An action ${pending} is already pending while trying to write!")
      throw new Exception(s"An action ${pending} is already pending while trying to write!")
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
    logger.info("Performing read...")
    if (pending != None) {
      logger.error(s"An action ${pending} is already pending while trying to read!")
      throw new Exception(s"An action ${pending} is already pending while trying to read!")
    }
    val promise = Promise[Array[Byte]]()
    pending = Some(
      PendingRead(
        promise = promise,
        query = query,
        resendTimer = makePendingReadResendTimer(query),
        resendRinseTimer = makeResendRinseTimer(query),
        latestIndex = 0,
        latestCommitted = 0,
        response = Array[Byte](0),
        quorumResponses = Set()
      )
    )
    transport.executionContext().execute(() => readImpl(query))
    promise.future
  }
}
