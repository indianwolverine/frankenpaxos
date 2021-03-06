package frankenpaxos.raft

import scala.collection.mutable
import scala.scalajs.js.annotation._
import scala.scalajs.js.JSConverters._
import scala.scalajs.js
import scala.concurrent.Future
import frankenpaxos.Actor
import frankenpaxos.JsLogger
import frankenpaxos.JsTransport
import frankenpaxos.JsTransportAddress
import frankenpaxos.statemachine.{
  KeyValueStore,
  GetRequest,
  KeyValueStoreInput,
  SetKeyValuePair,
  SetRequest,
  KeyValueStoreOutputSerializer,
  KeyValueStoreOutput,
}
@JSExportAll
class Raft {
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

  // Clients.
  val client1Logger = new JsLogger()
  val client1 = new Client[JsTransport](
    JsTransportAddress("Client 1"),
    transport,
    client1Logger,
    config
  )

  val client2Logger = new JsLogger()
  val client2 = new Client[JsTransport](
    JsTransportAddress("Client 2"),
    transport,
    client2Logger,
    config
  )

  val client3Logger = new JsLogger()
  val client3 = new Client[JsTransport](
    JsTransportAddress("Client 3"),
    transport,
    client3Logger,
    config
  )

  // Participants.
  val participant1Logger = new JsLogger()
  val participant1 = new Participant[JsTransport](
    JsTransportAddress("Participant 1"),
    transport,
    participant1Logger,
    config,
    new KeyValueStore(),
    participantIndex = 0
  )

  val participant2Logger = new JsLogger()
  val participant2 = new Participant[JsTransport](
    JsTransportAddress("Participant 2"),
    transport,
    participant2Logger,
    config,
    new KeyValueStore(),
    participantIndex = 1
  )

  val participant3Logger = new JsLogger()
  val participant3 = new Participant[JsTransport](
    JsTransportAddress("Participant 3"),
    transport,
    participant3Logger,
    config,
    new KeyValueStore(),
    participantIndex = 2
  )

  val participant4Logger = new JsLogger()
  val participant4 = new Participant[JsTransport](
    JsTransportAddress("Participant 4"),
    transport,
    participant4Logger,
    config,
    new KeyValueStore(),
    participantIndex = 3
  )

  val participant5Logger = new JsLogger()
  val participant5 = new Participant[JsTransport](
    JsTransportAddress("Participant 5"),
    transport,
    participant5Logger,
    config,
    new KeyValueStore(),
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

  def toPromise(future: Future[Array[Byte]]): js.Promise[Array[Byte]] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    future.toJSPromise
  }

  def deserializeOutput(bytes: Array[Byte]): String = {
    import KeyValueStoreOutput.Reply
    val kvoutput: KeyValueStoreOutput = KeyValueStoreOutputSerializer.fromBytes(bytes)
    kvoutput.reply match {
      case Reply.GetReply(pairs) =>
        return pairs.toString()
      case Reply.SetReply(content) =>
        return content.toString()
      case Reply.Empty => "Empty"
    }
  }
}

@JSExportAll
@JSExportTopLevel("frankenpaxos.raft.TweenedRaft")
object TweenedRaft {
  val Raft = new Raft();
}
