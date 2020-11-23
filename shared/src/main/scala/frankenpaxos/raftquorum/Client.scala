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

//   // The set of raft nodes.
//   val raftParticipants: Seq[Chan[QuorumParticipant[Transport]]] =
//     for (participantAddress <- config.participantAddresses)
//       yield chan[QuorumParticipant[Transport]](
//         participantAddress,
//         QuorumParticipant.serializer
//       )

//   // index of presumed leader
//   var leaderIndex = 0

//   // random
//   val rand = new Random();

//   // read quorum
//   var readQuorum = Set[Transport#Address]();

//   // response
//   var response = "";

//   // latest accepted
//   var latestAccepted = 0;

//   // latest committed 
//   var latestCommitted = 0;

//   // pending action - No need for lock since client acts synchronously
//   @JSExportAll
//   sealed trait PendingState

//   @JSExportAll
//   case class PendingWrite(
//       promise: Promise[Boolean],
//       cmd: String,
//       resendTimer: Transport#Timer
//   ) extends PendingState

//   @JSExportAll
//   case class PendingRead(
//       promise: Promise[String],
//       index: Integer,
//       resendTimer: Transport#Timer
//   ) extends PendingState

//   var pending: Option[PendingState] = None

//   logger.info(s"Raft Quorum client listening on $srcAddress.")

  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    // import QuorumClientInbound.Request
    // inbound.request match {
    //   case Request.ClientRequestResponse(r) =>
    //     handleClientRequestResponse(src, r)
    //   case Request.ClientQuorumQueryResponse(r) =>
    //     handleClientQuorumQueryResponse(src, r)
    //   case Request.Empty => {
    //     logger.fatal("Empty ClientInbound encountered.")
    //   }
    // }
  }

//   // Helpers

//   private def makePendingWriteResendTimer(
//       cmd: String
//   ): Transport#Timer = {
//     lazy val t: Transport#Timer = timer(
//       s"resendPendingWrite",
//       java.time.Duration.ofSeconds(10),
//       () => {
//         leaderIndex = rand.nextInt(raftParticipants.size)
//         writeImpl(cmd)
//         t.start()
//       }
//     )
//     t.start()
//     t
//   }

//   private def makePendingReadResendTimer(
//       index: Integer
//   ): Transport#Timer = {
//     lazy val t: Transport#Timer = timer(
//       s"resendPendingRead",
//       java.time.Duration.ofSeconds(10),
//       () => {
//         leaderIndex = rand.nextInt(raftParticipants.size)
//         readImpl(index)
//         t.start()
//       }
//     )
//     t.start()
//     t
//   }

//   private def handleClientRequestResponse(
//       src: Transport#Address,
//       requestResponse: frankenpaxos.raft.ClientRequestResponse
//   ) {
//     logger.info(
//       s"Got ClientRequestResponse from ${src}"
//         + s"| Success: ${requestResponse.success}"
//         + s"| Response: ${requestResponse.response}"
//         + s"| Leader Hint: ${requestResponse.leaderHint}"
//     )
//     pending match {
//       case Some(pendingWrite: PendingWrite) =>
//         pendingWrite.resendTimer.stop()
//         if (!requestResponse.success) {
//           if (requestResponse.response == "NOT_LEADER") {
//             leaderIndex = requestResponse.leaderHint
//             logger.info(
//               s"$src is not leader, trying again with ${raftParticipants(leaderIndex).dst}."
//             )
//             writeImpl(pendingWrite.cmd)
//           } else {
//             logger.info(
//               s"PendingWrite failed: ${pendingWrite}."
//             )
//             pendingWrite.promise.failure(new Exception("Write failed"))
//           }
//         } else {
//           logger.info("Command successfully replicated!")
//           pendingWrite.promise.success(true)
//           pending = None
//         }
//       case Some(_: PendingRead) =>
//         logger.error("Request response received while no pending read exists.")
//       case None =>
//         logger.error(
//           "Request response received while no pending action exists."
//         )
//     }
//   }

//   private def handleClientQuorumQueryResponse(
//       src: Transport#Address,
//       quorumQueryResponse: ClientQuorumQueryResponse
//   ) {
//     logger.info(
//       s"Got ClientQuorumQueryResponse from ${src}"
//         + s"| Success: ${quorumQueryResponse.success}"
//         + s"| Latest Accepted: ${quorumQueryResponse.latestAccepted}"
//         + s"| Latest Committed: ${quorumQueryResponse.latestCommitted}"
//         + s"| Response ${quorumQueryResponse.response}"
//     )
//     pending match {
//       case Some(_: PendingWrite) =>
//         logger.error("Request response received while no pending write exists.")
//       case Some(pendingRead: PendingRead) =>
//         if (quorumQueryResponse.success) {
//           pendingRead.resendTimer.reset()
//           readQuorum += src
//           if (quorumQueryResponse.latestAccepted >= latestAccepted) {
//             latestAccepted = quorumQueryResponse.latestAccepted
//             latestCommitted = latestCommitted.max(quorumQueryResponse.latestCommitted)
//             response = quorumQueryResponse.response
//           }
//           // a quorum has replied or we are rinsing a read
//           if (readQuorum.size >= (raftParticipants.size / 2 + 1) || pendingRead.index > 0) {
//             pendingRead.resendTimer.stop()
//             // no need to rinse
//             if (latestCommitted >= latestAccepted) {
//               readQuorum = Set[Transport#Address]()
//               latestAccepted = 0
//               latestCommitted = 0
//               response = ""
//               pendingRead.promise.success(response)
//               pending = None
//             } else { // rinse read
//               pending = Some(
//                 PendingRead(
//                     promise = Promise[String](),
//                     index = latestAccepted,
//                     resendTimer = makePendingReadResendTimer(latestAccepted)
//                 )
//               )
//               readImpl(latestAccepted)
//             }
//           } 
//         }
//       case None =>
//         logger.error(
//           "Request response received while no pending action exists."
//         )
//     }
//   }

//   private def writeImpl(cmd: String): Unit = {
//     raftParticipants(leaderIndex).send(
//       QuorumParticipantInbound().withClientRequest(
//         frankenpaxos.raft.ClientRequest(cmd = cmd)
//       )
//     )
//   }

//   private def readImpl(index: Int): Unit = {
//     if (index == 0) {
//       // use shuffle to get n/2 + 1 random indices
//       val participantsRandomized = rand.shuffle(raftParticipants)

//       for (i <- 0 until (raftParticipants.size / 2) + 1) {
//         participantsRandomized(i).send(
//           QuorumParticipantInbound().withClientQuorumQuery(
//             ClientQuorumQuery(index = index)
//           )
//         )
//       }
//     } else if (index > 0) { // rinse index
//       val participantIndex = rand.nextInt(raftParticipants.size)
//       raftParticipants(participantIndex).send(
//         QuorumParticipantInbound().withClientQuorumQuery(
//             ClientQuorumQuery(index = index)
//         )
//       )
//     }
//   }

//   // Interface

//   def write(cmd: String): Future[Boolean] = {
//     if (pending != None) {
//       throw new Exception("An action is already pending!")
//     }
//     val promise = Promise[Boolean]()
//     pending = Some(
//       PendingWrite(
//         promise = promise,
//         cmd = cmd,
//         resendTimer = makePendingWriteResendTimer(cmd)
//       )
//     )
//     transport.executionContext().execute(() => writeImpl(cmd))
//     promise.future
//   }

//   def read(index: Int): Future[String] = {
//     if (pending != None) {
//       throw new Exception("An action is already pending!")
//     }
//     val promise = Promise[String]()
//     pending = Some(
//       PendingRead(
//         promise = promise,
//         index = index,
//         resendTimer = makePendingReadResendTimer(index)
//       )
//     )
//     transport.executionContext().execute(() => readImpl(index))
//     promise.future
//   }
}
