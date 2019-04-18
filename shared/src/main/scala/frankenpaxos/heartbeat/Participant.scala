// Often in a distributed system, one node wants to know whether another node
// is dead or alive. In general, a failure detector that detects failures
// perfectly is impossible, but we can implement a best-effort failure detector
// based on heartbeats.
//
// Here's the set up. We are given a set of nodes, and we run a heartbeat
// participant on every node. Every participant sends empty heartbeat pings to
// the other heartbeats. When a participant receives a heartbeat ping, it
// responds with a heartbeat pong. If a participant `a` sends pings to another
// participant `b` but does not receive pongs, then `a` concludes that `b` is
// dead.

package frankenpaxos.heartbeat

import collection.mutable
import frankenpaxos.Actor
import frankenpaxos.Chan
import frankenpaxos.Logger
import frankenpaxos.ProtoSerializer
import scala.scalajs.js.annotation._

@JSExportAll
object ParticipantInboundSerializer
    extends ProtoSerializer[ParticipantInbound] {
  type A = ParticipantInbound
  override def toBytes(x: A): Array[Byte] = super.toBytes(x)
  override def fromBytes(bytes: Array[Byte]): A = super.fromBytes(bytes)
  override def toPrettyString(x: A): String = super.toPrettyString(x)
}

@JSExportAll
object Participant {
  val serializer = ParticipantInboundSerializer
}

// These options govern how the heartbeat protocol operates. These options
// mimic the keepalive interval, keepalive time, and keepalive retry options
// used to configure TCP keepalive.
case class HeartbeatOptions(
    // After a participant sends a ping, it waits `failPeriod` time for a pong.
    // If no pong comes, the participant sends another ping and again waits
    // `failPeriod` time.
    failPeriod: java.time.Duration,
    // If a heartbeat participant sends a ping and does receive a pong within
    // `failPeriod` time, then it waits `successPeriod` time to send another
    // ping.
    successPeriod: java.time.Duration,
    // If a participant has sent `numRetries` consecutive pings to some
    // participant `a` and not received a pong, then the participant deems `a`
    // as dead.
    numRetries: Int,
    // Heartbeat participants maintain an exponential weighted average of the
    // network delay to other nodes. `networkDelayAlpha` defines the decay
    // factor of this average. More explicitly, letting delay_t be the
    // estimated delay at time t:
    //
    //   delay_t = (networkDelayAlpha * new_delay) +
    //             ((1 - networkDelayAlpha) * delay_{t-1})
    networkDelayAlpha: Double
)

object HeartbeatOptions {
  val default = HeartbeatOptions(
    failPeriod = java.time.Duration.ofSeconds(5),
    successPeriod = java.time.Duration.ofSeconds(10),
    numRetries = 3,
    networkDelayAlpha = 0.9
  )
}

@JSExportAll
class Participant[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    val addresses: Set[Transport#Address],
    options: HeartbeatOptions = HeartbeatOptions.default
) extends Actor(address, transport, logger) {
  // Sanity check options.
  logger.check_le(0, options.networkDelayAlpha)
  logger.check_le(options.networkDelayAlpha, 1)

  override type InboundMessage = ParticipantInbound
  override val serializer = ParticipantInboundSerializer

  private val chans: Map[Transport#Address, Chan[Participant[Transport]]] = {
    for (a <- addresses)
      yield a -> chan[Participant[Transport]](a, Participant.serializer)
  }.toMap

  // When a participant sents a heartbeat ping, it sets a fail timer. If it
  // doesn't hear back before the timer expires, it sends another. If it does,
  // it sets a success timer to send another ping.
  private val failTimers: Map[Transport#Address, Transport#Timer] = {
    for (a <- addresses)
      yield a -> timer(s"failTimer$a", options.failPeriod, () => fail(a))
  }.toMap

  // Timers that are set after a participant receives a pong.
  private val successTimers: Map[Transport#Address, Transport#Timer] = {
    for (a <- addresses)
      yield
        a -> timer(s"successTimer$a", options.successPeriod, () => succeed(a))
  }.toMap

  // The number of unacknowledged retries sent to every participant.
  @JSExport
  protected val numRetries: mutable.Map[Transport#Address, Int] = mutable.Map()
  for (a <- addresses) {
    numRetries(a) = 0
  }

  // The estimated delay between this node and every other node.
  @JSExport
  protected var networkDelayNanos = Map[Transport#Address, Double]()

  // The addresses of participants that are alive.
  @JSExport
  protected val alive: mutable.Set[Transport#Address] =
    mutable.Set() ++ addresses

  // Send a ping to every participant and start the timers.
  for ((a, chan) <- chans) {
    chan.send(ParticipantInbound().withPing(Ping(System.nanoTime)))
    failTimers(a).start()
  }

  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    import ParticipantInbound.Request
    inbound.request match {
      case Request.Ping(r) => handlePing(src, r)
      case Request.Pong(r) => handlePong(src, r)
      case Request.Empty => {
        logger.fatal("Empty ParticipantInbound encountered.")
      }
    }
  }

  private def handlePing(src: Transport#Address, ping: Ping): Unit = {
    val participant = chan[Participant[Transport]](src, Participant.serializer)
    participant.send(ParticipantInbound().withPong(Pong(ping.nanotime)))
  }

  private def handlePong(src: Transport#Address, pong: Pong): Unit = {
    val delayNanos = (System.nanoTime - pong.nanotime) / 2
    networkDelayNanos.get(src) match {
      case Some(x) =>
        val newAverage =
          (options.networkDelayAlpha * delayNanos) +
            ((1 - options.networkDelayAlpha) * x)
        networkDelayNanos += (src -> newAverage)

      case None =>
        networkDelayNanos += (src -> delayNanos)
    }

    alive += src
    numRetries(src) = 0
    failTimers(src).stop()
    successTimers(src).start()
  }

  private def fail(a: Transport#Address): Unit = {
    numRetries(a) += 1
    if (numRetries(a) >= options.numRetries) {
      alive -= a
    }
    chans(a).send(ParticipantInbound().withPing(Ping(System.nanoTime)))
    failTimers(a).start()
  }

  private def succeed(a: Transport#Address): Unit = {
    chans(a).send(ParticipantInbound().withPing(Ping(System.nanoTime)))
    failTimers(a).start()
  }

  // Returns the network delay to every node. If a node is not alive, the delay
  // is set to infinity. Note that this method MUST only ever be called from an
  // actor running on the same transport.
  def unsafeNetworkDelay(): Map[Transport#Address, java.time.Duration] = {
    // See https://stackoverflow.com/a/38228657/3187068.
    val maxDuration = java.time.Duration.ofSeconds(Long.MaxValue, 999999999)

    {
      for (address <- addresses) yield {
        val delay = networkDelayNanos
          .get(address)
          .map(_.toLong)
          .map(java.time.Duration.ofNanos(_))
          .getOrElse(maxDuration)
        (address, delay)
      }
    }.toMap
  }

  // Returns the set of addresses that this participant thinks are alive. Note
  // that this method MUST only ever be called from an actor running on the
  // same transport.
  def unsafeAlive(): Set[Transport#Address] = Set() ++ alive
}
