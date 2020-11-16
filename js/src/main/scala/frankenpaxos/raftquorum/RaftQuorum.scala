package frankenpaxos.raftquorum

import scala.collection.mutable
import scala.scalajs.js.annotation._
import frankenpaxos.Actor
import frankenpaxos.JsLogger
import frankenpaxos.JsTransport
import frankenpaxos.JsTransportAddress

@JSExportAll
class RaftQuorum {
  // Transport.
  val logger = new JsLogger()
  val transport = new JsTransport(logger);

  // Configuration.
  val config = Config[JsTransport](
    participantAddresses = List(
      JsTransportAddress("Participant 1"),
      JsTransportAddress("Participant 2"),
      JsTransportAddress("Participant 3"),
      JsTransportAddress("Participant 4"),
      JsTransportAddress("Participant 5"),
    ),
    clientAddresses = List(
      JsTransportAddress("Client 1"),
      JsTransportAddress("Client 2"),
      JsTransportAddress("Client 3")
    )
  )

  // Clients.
  val client1Logger = new JsLogger()
  val client1 = new QuorumClient[JsTransport](
    JsTransportAddress("Client 1"),
    transport,
    client1Logger,
    config
  )

  val client2Logger = new JsLogger()
  val client2 = new QuorumClient[JsTransport](
    JsTransportAddress("Client 2"),
    transport,
    client2Logger,
    config
  )

  val client3Logger = new JsLogger()
  val client3 = new QuorumClient[JsTransport](
    JsTransportAddress("Client 3"),
    transport,
    client3Logger,
    config
  )

  // Participants.
  val participant1Logger = new JsLogger()
  val participant1 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 1"),
    transport,
    participant1Logger,
    config
  )

  val participant2Logger = new JsLogger()
  val participant2 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 2"),
    transport,
    participant2Logger,
    config
  )

  val participant3Logger = new JsLogger()
  val participant3 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 3"),
    transport,
    participant3Logger,
    config
  )

  val participant4Logger = new JsLogger()
  val participant4 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 4"),
    transport,
    participant4Logger,
    config
  )
  
  val participant5Logger = new JsLogger()
  val participant5 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 5"),
    transport,
    participant5Logger,
    config
  )
}

@JSExportAll
@JSExportTopLevel("frankenpaxos.raftquorum.TweenedRaftQuorum")
object TweenedRaftQuorum {
  val RaftQuorum = new RaftQuorum();
}
