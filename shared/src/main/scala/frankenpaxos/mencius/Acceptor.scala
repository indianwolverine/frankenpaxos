package frankenpaxos.mencius

import collection.mutable
import frankenpaxos.Actor
import frankenpaxos.Chan
import frankenpaxos.Logger
import frankenpaxos.ProtoSerializer
import frankenpaxos.monitoring.Collectors
import frankenpaxos.monitoring.Counter
import frankenpaxos.monitoring.PrometheusCollectors
import frankenpaxos.monitoring.Summary
import frankenpaxos.roundsystem.RoundSystem
import scala.scalajs.js.annotation._
import scala.util.Random

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
case class AcceptorOptions(
    measureLatencies: Boolean
)

@JSExportAll
object AcceptorOptions {
  val default = AcceptorOptions(
    measureLatencies = true
  )
}

@JSExportAll
class AcceptorMetrics(collectors: Collectors) {
  val requestsTotal: Counter = collectors.counter
    .build()
    .name("mencius_acceptor_requests_total")
    .labelNames("type")
    .help("Total number of processed requests.")
    .register()

  val requestsLatency: Summary = collectors.summary
    .build()
    .name("mencius_acceptor_requests_latency")
    .labelNames("type")
    .help("Latency (in milliseconds) of a request.")
    .register()
}

@JSExportAll
class Acceptor[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport],
    options: AcceptorOptions = AcceptorOptions.default,
    metrics: AcceptorMetrics = new AcceptorMetrics(PrometheusCollectors)
) extends Actor(address, transport, logger) {
  config.checkValid()

  // Types /////////////////////////////////////////////////////////////////////
  override type InboundMessage = AcceptorInbound
  override val serializer = AcceptorInboundSerializer

  type Slot = Int

  @JSExportAll
  case class State(
      voteRound: Int,
      voteValue: CommandBatchOrNoop
  )

  // Fields ////////////////////////////////////////////////////////////////////
  // Leader channels.
  private val leaders: Seq[Seq[Chan[Leader[Transport]]]] =
    for (group <- config.leaderAddresses) yield {
      for (address <- group)
        yield chan[Leader[Transport]](address, Leader.serializer)
    }

  // The acceptor's indices.
  @JSExport
  protected val leaderGroupIndex: Int =
    config.acceptorAddresses.indexWhere(
      groups => groups.exists(_.contains(address))
    )
  @JSExport
  protected val acceptorGroupIndex: Int =
    config.acceptorAddresses(leaderGroupIndex).indexWhere(_.contains(address))
  @JSExport
  protected val index = config
    .acceptorAddresses(leaderGroupIndex)(acceptorGroupIndex)
    .indexOf(address)

  // The round system for this leader group.
  private val roundSystem = new RoundSystem.ClassicRoundRobin(
    config.leaderAddresses(leaderGroupIndex).size
  )

  // A round system used to figure out which leader groups are in charge of
  // which slots. For example, if we have 5 leader groups and we're leader
  // group 1 and we'd like to know which slot to use after slot 20, we can call
  // slotSystem.nextClassicRound(1, 20).
  private val slotSystem =
    new RoundSystem.ClassicRoundRobin(config.numLeaderGroups)

  @JSExport
  protected var round: Int = -1

  @JSExport
  protected var states = mutable.SortedMap[Slot, State]()

  // Helpers ///////////////////////////////////////////////////////////////////
  private def timed[T](label: String)(e: => T): T = {
    if (options.measureLatencies) {
      val startNanos = System.nanoTime
      val x = e
      val stopNanos = System.nanoTime
      metrics.requestsLatency
        .labels(label)
        .observe((stopNanos - startNanos).toDouble / 1000000)
      x
    } else {
      e
    }
  }

  private def acceptorGroupIndexBySlot(slot: Slot): Int = {
    (slot / config.numLeaderGroups) %
      config.acceptorAddresses(leaderGroupIndex).size
  }

  // Handlers //////////////////////////////////////////////////////////////////
  override def receive(src: Transport#Address, inbound: InboundMessage) = {
    import AcceptorInbound.Request

    val label =
      inbound.request match {
        case Request.Phase1A(_)          => "Phase1a"
        case Request.Phase2A(_)          => "Phase2a"
        case Request.Phase2ANoopRange(_) => "Phase2aNoopRange"
        case Request.Empty =>
          logger.fatal("Empty AcceptorInbound encountered.")
      }
    metrics.requestsTotal.labels(label).inc()

    timed(label) {
      inbound.request match {
        case Request.Phase1A(r)          => handlePhase1a(src, r)
        case Request.Phase2A(r)          => handlePhase2a(src, r)
        case Request.Phase2ANoopRange(r) => handlePhase2aNoopRange(src, r)
        case Request.Empty =>
          logger.fatal("Empty AcceptorInbound encountered.")
      }
    }
  }

  private def handlePhase1a(
      src: Transport#Address,
      phase1a: Phase1a
  ): Unit = {
    val leader = chan[Leader[Transport]](src, Leader.serializer)

    // If we receive an out of date round, we send back a nack.
    if (phase1a.round < round) {
      logger.debug(
        s"An acceptor received a Phase1a message in round ${phase1a.round} " +
          s"but is in round $round."
      )
      leader.send(LeaderInbound().withNack(Nack(round = round)))
      return
    }

    // Otherwise, we update our round and send back a Phase1b message to the
    // leader.
    round = phase1a.round
    val phase1b = Phase1b(
      groupIndex = acceptorGroupIndex,
      acceptorIndex = index,
      round = round,
      info = states
        .iteratorFrom(phase1a.chosenWatermark)
        .map({
          case (slot, state) =>
            Phase1bSlotInfo(slot = slot,
                            voteRound = state.voteRound,
                            voteValue = state.voteValue)
        })
        .toSeq
    )
    leader.send(LeaderInbound().withPhase1B(phase1b))
  }

  private def handlePhase2a(
      src: Transport#Address,
      phase2a: Phase2a
  ): Unit = {
    // If we receive an out of date round, we send back a nack to the leader.
    // Note that `src` is the address of the proxy leader, not the leader. We
    // don't want to send nacks to the proxy leader; we want to send them to
    // the actual leader.
    if (phase2a.round < round) {
      logger.debug(
        s"An acceptor received a Phase2a message in round ${phase2a.round} " +
          s"but is in round $round."
      )
      val leader = leaders(slotSystem.leader(phase2a.slot))(
        roundSystem.leader(phase2a.round)
      )
      leader.send(LeaderInbound().withNack(Nack(round = round)))
      return
    }

    // Otherwise, update our round and send back a Phase2b message to the proxy
    // leader.
    round = phase2a.round
    states(phase2a.slot) = State(
      voteRound = round,
      voteValue = phase2a.commandBatchOrNoop
    )
    val proxyLeader = chan[ProxyLeader[Transport]](src, ProxyLeader.serializer)
    proxyLeader.send(
      ProxyLeaderInbound().withPhase2B(
        Phase2b(acceptorIndex = index, slot = phase2a.slot, round = round)
      )
    )
  }

  private def handlePhase2aNoopRange(
      src: Transport#Address,
      phase2a: Phase2aNoopRange
  ): Unit = {
    // If we receive an out of date round, we send back a nack to the leader.
    // Note that `src` is the address of the proxy leader, not the leader. We
    // don't want to send nacks to the proxy leader; we want to send them to
    // the actual leader.
    if (phase2a.round < round) {
      logger.debug(
        s"An acceptor received a Phase2aNoopRange message in round " +
          s"${phase2a.round} but is in round $round."
      )
      val leader = leaders(slotSystem.leader(phase2a.slotStartInclusive))(
        roundSystem.leader(phase2a.round)
      )
      leader.send(LeaderInbound().withNack(Nack(round = round)))
      return
    }

    // Otherwise, update our round and send back a Phase2bNoopRange message to
    // the proxy leader.
    round = phase2a.round

    // Find the first slot owned by this acceptor group. This isn't the fastest
    // way to do this, but it's simple.
    var startSlot = phase2a.slotStartInclusive
    while (acceptorGroupIndexBySlot(startSlot) != acceptorGroupIndex) {
      startSlot += config.numLeaderGroups
    }

    for (slot <- startSlot until
           phase2a.slotEndExclusive by
           config.numLeaderGroups * config
             .acceptorAddresses(leaderGroupIndex)
             .size) {
      states(slot) = State(
        voteRound = round,
        voteValue = CommandBatchOrNoop().withNoop(Noop())
      )
    }

    val proxyLeader = chan[ProxyLeader[Transport]](src, ProxyLeader.serializer)
    proxyLeader.send(
      ProxyLeaderInbound().withPhase2BNoopRange(
        Phase2bNoopRange(
          acceptorGroupIndex = acceptorGroupIndex,
          acceptorIndex = index,
          slotStartInclusive = phase2a.slotStartInclusive,
          slotEndExclusive = phase2a.slotEndExclusive,
          round = round
        )
      )
    )
  }
}
