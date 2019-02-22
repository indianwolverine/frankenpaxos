package frankenpaxos.fastpaxos

import frankenpaxos.Actor
import frankenpaxos.Logger
import frankenpaxos.ProtoSerializer
import frankenpaxos.Chan
import scala.scalajs.js.annotation._

@JSExportAll
object AcceptorInboundSerializer extends ProtoSerializer[AcceptorInbound] {
  type A = AcceptorInbound
  override def toBytes(x: A): Array[Byte] = super.toBytes(x)
  override def fromBytes(bytes: Array[Byte]): A = super.fromBytes(bytes)
  override def toPrettyString(x: A): String = super.toPrettyString(x)
}

@JSExportAll
object Acceptor {
  val serializer = AcceptorInboundSerializer
}

@JSExportAll
class Acceptor[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport]
) extends Actor(address, transport, logger) {
  override type InboundMessage = AcceptorInbound
  override val serializer = AcceptorInboundSerializer

  // Sanity check the Paxos configuration and retrieve acceptor index.
  logger.check(config.acceptorAddresses.contains(address))
  private val index = config.acceptorAddresses.indexOf(address)

  // The largest round in which this acceptor has received a message.
  @JSExport
  protected var round: Int = -1;

  // The largest round in which this acceptor has voted.
  @JSExport
  protected var voteRound: Int = -1;

  // A Fast Paxos acceptor can receive values from clients and leaders. A Fast
  // Paxos acceptor can also receive a designated `any` value from a leader. In
  // this case, a Fast Paxos acceptor votes for the next value that it receives.
  // And, initially, a Fast Paxos acceptor hasn't voted for anything. These
  // values are represented by VoteValue.
  sealed trait VoteValue
  case class Value(v: String) extends VoteValue
  case object Nothing extends VoteValue
  case object Any extends VoteValue
  @JSExport
  protected var voteValue: VoteValue = Nothing

  override def receive(src: Transport#Address, inbound: InboundMessage) = {
    import AcceptorInbound.Request
    inbound.request match {
      case Request.ProposeRequest(r) => handleProposeRequest(src, r)
      case Request.Phase1A(r)        => handlePhase1a(src, r)
      case Request.Phase2A(r)        => handlePhase2a(src, r)
      case Request.Empty => {
        logger.fatal("Empty AcceptorInbound encountered.")
      }
    }
  }

  private def handleProposeRequest(
      src: Transport#Address,
      proposeRequest: ProposeRequest
  ): Unit = {
    // If we receive a value from a client, we ignore it unless we have
    // received the distinguished any value from the leader. In that case, we
    // vote for it.
    voteValue match {
      case Any =>
        voteRound = round
        voteValue = Value(proposeRequest.v)
        val client = chan[Client[Transport]](src, Client.serializer)
        client.send(
          ClientInbound().withPhase2B(
            Phase2b(acceptorId = index, round = round)
          )
        )
      case Value(_) | Nothing =>
    }
  }

  private def handlePhase1a(src: Transport#Address, phase1a: Phase1a): Unit = {
    // Ignore messages from previous rounds.
    if (phase1a.round <= round) {
      logger.info(
        s"An acceptor received a phase 1a message for round " +
          s"${phase1a.round} but is in round $round."
      )
      return
    }

    // Bump our round and send the leader our vote round and vote value.
    round = phase1a.round
    val optionalVoteValue = voteValue match {
      case Value(v)      => Some(v)
      case Nothing | Any => None
    }
    val leader = chan[Leader[Transport]](src, Leader.serializer)
    leader.send(
      LeaderInbound().withPhase1B(
        Phase1b(round = round, acceptorId = index, voteRound = voteRound)
          .update(_.optionalVoteValue := optionalVoteValue)
      )
    )
  }

  private def handlePhase2a(src: Transport#Address, phase2a: Phase2a): Unit = {
    // Ignore messages from smaller rounds.
    if (phase2a.round < round) {
      logger.info(
        s"An acceptor received a phase 2a message for round " +
          s"${phase2a.round} but is in round $round."
      )
      return
    }

    // Ignore messages from our current round if we've already voted.
    if (phase2a.round == round && phase2a.round == voteRound) {
      logger.info(
        s"An acceptor received a phase 2a message for round " +
          s"${phase2a.round} but has already voted in round $round."
      )
      return
    }

    // If the leader sends us the designated `any` value, then we vote for the
    // next thing that we receive. Otherwise, we vote now.
    phase2a.value match {
      case Some(v) =>
        round = phase2a.round
        voteRound = phase2a.round
        voteValue = Value(v)

        val leader = chan[Leader[Transport]](src, Leader.serializer)
        leader.send(
          LeaderInbound().withPhase2B(
            Phase2b(acceptorId = index, round = round)
          )
        )
      case None =>
        round = phase2a.round
        voteValue = Any
    }
  }
}
