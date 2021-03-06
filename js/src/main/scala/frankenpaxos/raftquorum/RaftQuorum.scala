package frankenpaxos.raftquorum

import scala.collection.mutable
import scala.scalajs.js.annotation._
import frankenpaxos.Actor
import frankenpaxos.JsLogger
import frankenpaxos.JsTransport
import frankenpaxos.JsTransportAddress
import frankenpaxos.quorums.SimpleMajority
import frankenpaxos.statemachine.{
  GetRequest,
  KeyValueStore,
  KeyValueStoreInput,
  SetKeyValuePair,
  SetRequest
}
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
      JsTransportAddress("Participant 5")
    ),
    clientAddresses = List(
      JsTransportAddress("Client 1"),
      JsTransportAddress("Client 2"),
      JsTransportAddress("Client 3")
    )
  )
  val quorumSystem = new SimpleMajority(
    (0 until config.participantAddresses.size).toSet,
  )

  // Clients.
  val client1Logger = new JsLogger()
  val client1 = new QuorumClient[JsTransport](
    JsTransportAddress("Client 1"),
    transport,
    client1Logger,
    config,
    quorumSystem
  )

  val client2Logger = new JsLogger()
  val client2 = new QuorumClient[JsTransport](
    JsTransportAddress("Client 2"),
    transport,
    client2Logger,
    config,
    quorumSystem
  )

  val client3Logger = new JsLogger()
  val client3 = new QuorumClient[JsTransport](
    JsTransportAddress("Client 3"),
    transport,
    client3Logger,
    config,
    quorumSystem
  )

  // Participants.
  val participant1Logger = new JsLogger()
  val participant1 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 1"),
    transport,
    participant1Logger,
    config,
    new KeyValueStore(),
    quorumSystem,
    participantIndex = 0
  )

  val participant2Logger = new JsLogger()
  val participant2 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 2"),
    transport,
    participant2Logger,
    config,
    new KeyValueStore(),
    quorumSystem,
    participantIndex = 1
  )

  val participant3Logger = new JsLogger()
  val participant3 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 3"),
    transport,
    participant3Logger,
    config,
    new KeyValueStore(),
    quorumSystem,
    participantIndex = 2
  )

  val participant4Logger = new JsLogger()
  val participant4 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 4"),
    transport,
    participant4Logger,
    config,
    new KeyValueStore(),
    quorumSystem,
    participantIndex = 3
  )

  val participant5Logger = new JsLogger()
  val participant5 = new QuorumParticipant[JsTransport](
    JsTransportAddress("Participant 5"),
    transport,
    participant5Logger,
    config,
    new KeyValueStore(),
    quorumSystem,
    participantIndex = 4
  )

  def serializeWrite(key: String, value: String): Array[Byte] = {
    KeyValueStoreInput()
      .withSetRequest(
        SetRequest(keyValue = Seq(SetKeyValuePair(key = key, value = value)))
      )
      .toByteArray
  }

  def serializeRead(key: String): Array[Byte] = {
    KeyValueStoreInput()
      .withGetRequest(GetRequest(key = Seq(key)))
      .toByteArray
  }
}

@JSExportAll
@JSExportTopLevel("frankenpaxos.raftquorum.TweenedRaftQuorum")
object TweenedRaftQuorum {
  val RaftQuorum = new RaftQuorum();
}
