package frankenpaxos.raft

import frankenpaxos.Actor
import frankenpaxos.Chan
import frankenpaxos.Logger
import frankenpaxos.ProtoSerializer
import frankenpaxos.Util
import scala.collection.mutable
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

// When a node is voted the leader for a particular term, it begins sending
// pings to the other nodes. If a follower has not heard a ping from the leader
// in a sufficiently long time, it becomes a candidate and attempts to become a
// leader in a higher term. If a candidate has not received sufficiently many
// votes after a given timeout, it becomes a candidate in a higher term.
//
// Leaders send pings every `pingPeriod` seconds. Similarly, a follower will
// wait between `noPingTimeoutMin` and `noPingTimeoutMax` seconds after hearing
// a ping before becoming a candidate. The actual time waited is chosen
// uniformly at random to avoid collisions. Similarly, a candidate waits
// between `noVoteTimeoutMin` and `noVoteTimeoutMax` seconds to become a
// candidate at a higher term.
case class ElectionOptions(
    pingPeriod: java.time.Duration,
    noPingTimeoutMin: java.time.Duration,
    noPingTimeoutMax: java.time.Duration,
    notEnoughVotesTimeoutMin: java.time.Duration,
    notEnoughVotesTimeoutMax: java.time.Duration
)

object ElectionOptions {
  val default = ElectionOptions(
    pingPeriod = java.time.Duration.ofSeconds(1),
    noPingTimeoutMin = java.time.Duration.ofSeconds(10),
    noPingTimeoutMax = java.time.Duration.ofSeconds(12),
    notEnoughVotesTimeoutMin = java.time.Duration.ofSeconds(10),
    notEnoughVotesTimeoutMax = java.time.Duration.ofSeconds(12)
  )
}

@JSExportAll
class Participant[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    addresses: Set[Transport#Address],
    // A potential initial leader. If participants are initialized with a
    // leader, at most one leader should be set.
    leader: Option[Transport#Address] = None,
    options: ElectionOptions = ElectionOptions.default
) extends Actor(address, transport, logger) {
  // Possible states ///////////////////////////////////////////////////////////
  sealed trait ElectionState

  @JSExportAll
  case class LeaderlessFollower(
      noPingTimer: Transport#Timer
  ) extends ElectionState

  @JSExportAll
  case class Follower(
      noPingTimer: Transport#Timer,
      leader: Transport#Address
  ) extends ElectionState

  @JSExportAll
  case class Candidate(
      notEnoughVotesTimer: Transport#Timer,
      votes: Set[Transport#Address]
  ) extends ElectionState

  @JSExportAll
  case class Leader(pingTimer: Transport#Timer) extends ElectionState

  // Members ///////////////////////////////////////////////////////////////////
  override type InboundMessage = ParticipantInbound
  override def serializer = Participant.serializer

  // Sanity check arguments.
  logger.check(addresses.contains(address))
  logger.checkLe(options.noPingTimeoutMin, options.noPingTimeoutMax)
  logger.checkLe(options.notEnoughVotesTimeoutMin,
                 options.notEnoughVotesTimeoutMax)
  leader match {
    case Some(address) => logger.check(addresses.contains(address))
    case None          =>
  }

  // The addresses of the other participants.
  val nodes: Map[Transport#Address, Chan[Participant[Transport]]] = {
    for (a <- addresses)
      yield (a -> chan[Participant[Transport]](a, Participant.serializer))
  }.toMap

  // The callbacks to inform when a new leader is elected.
  var callbacks: mutable.Buffer[(Transport#Address) => Unit] = mutable.Buffer()

  // The current term.
  var term: Int = 0

  // The current state.
  var state: ElectionState = {
    leader match {
      case Some(leaderAddress) =>
        if (address == leaderAddress) {
          val t = pingTimer()
          t.start()
          Leader(t)
        } else {
          val t = noPingTimer()
          t.start()
          Follower(t, leaderAddress)
        }
      case None =>
        val t = noPingTimer()
        t.start()
        LeaderlessFollower(t)
    }
  }

  // The log
  var log: Array[LogEntry] = new Array[LogEntry](0)

  // The index of highest log entry known to be committed
  var commitIndex: Int = 0

  // The index of highest log entry applied to state machine
  var lastApplied: Int = 0

  // Leader State (reinit on election) /////////////////////////////////////////

  // index of next log entry to be sent to participant
  var nextIndex: Map[Transport#Address, Int] = {
    for (a <- addresses)
      yield (a -> 0)
  }.toMap

  // index of highest log entry known to be replicated on participant
  var matchIndex: Map[Transport#Address, Int] = {
    for (a <- addresses)
      yield (a -> 0)
  }.toMap
  

  // Callback registration /////////////////////////////////////////////////////
  def _register(callback: (Transport#Address) => Unit) = {
    callbacks += callback
  }

  def register(callback: (Transport#Address) => Unit) = {
    transport.executionContext.execute(() => _register(callback))
  }

  // Receive ///////////////////////////////////////////////////////////////////
  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    import ParticipantInbound.Request
    inbound.request match {
      case Request.AppendEntriesRequest(r)  => handleAppendEntriesRequest(src, r)
      case Request.AppendEntriesResponse(r) => handleAppendEntriesResponse(src, r)
      case Request.VoteRequest(r)           => handleVoteRequest(src, r)
      case Request.VoteResponse(r)          => handleVoteResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty LeaderInbound encountered.")
      }
    }
  }

  private def handleAppendEntriesRequest(src: Transport#Address, appReq: AppendEntriesRequest): Unit = {
    // If we hear a ping from an earlier term, return false and term.
    if (appReq.term < term) {
      nodes(src).send(ParticipantInbound().withAppendEntriesResponse(AppendEntriesResponse(term = term, success = false)))
      return
    }

    // If we hear from a leader in a larger term, then we immediately become a
    // follower of that leader.
    if (appReq.term > term) {
      transitionToFollower(appReq.term, src)
      return
    }

    // If our prevLogTerm doesn't match the requests', return false.
    if (getPrevLogTerm() != appReq.prevLogTerm) {
      nodes(src).send(ParticipantInbound().withAppendEntriesResponse(AppendEntriesResponse(term = term, success = false)))
      return
    }

    state match {
      case LeaderlessFollower(noPingTimer) => {
        transitionToFollower(appReq.term, src)
      }
      case Follower(noPingTimer, leader) => {
        // reset heartbeat timer
        noPingTimer.reset()

        // if this is not a hearbeat msg, main appendEntries logic
        if (appReq.entries.length > 0) {
          // If an existing entry conflicts with a new one (same index
          // but different terms), delete the existing entry and all that
          // follow it

          // Append any new entries not already in the log

          // If leaderCommit > commitIndex, set commitIndex =
          // min(leaderCommit, index of last new entry)
        }
      }
      case Candidate(notEnoughVotesTimer, votes) => {
        transitionToFollower(appReq.term, src)
      }
      case Leader(pingTimer) => {
        // We are the leader and received a ping from ourselves. We can just
        // ignore this ping.
      }
    }
  }

  private def handleAppendEntriesResponse(src: Transport#Address, appRes: AppendEntriesResponse): Unit = {
    // If we hear from a leader in a larger term, then we immediately become a
    // follower of that leader.
    if (appRes.term > term) {
      transitionToFollower(appRes.term, src)
      return
    }

    state match {
      case LeaderlessFollower(noPingTimer) => {
        logger.fatal(
          s"A leaderless follower recieved an AppendEntriesResponse."
        )
        return
      }
      case Follower(noPingTimer, leader) => {
        logger.fatal(
          s"A follower recieved an AppendEntriesResponse."
        )
        return
      }
      case Candidate(notEnoughVotesTimer, votes) => {
        logger.fatal(
          s"A candidate recieved an AppendEntriesResponse."
        )
        return
      }
      case Leader(pingTimer) => {
        if (appRes.success) {
          // update nextIndex and matchIndex for follower (src)
        }
        else {
          // decrement nextIndex for follower (src) and retry AppendEntriesRequest
        }
      }
    }

  }

  private def handleVoteRequest(
      src: Transport#Address,
      voteRequest: VoteRequest
  ): Unit = {
    // If we hear a vote request from an earlier term, reply with current term and don't grant vote.
    if (voteRequest.term < term) {
      nodes(src).send(ParticipantInbound().withVoteResponse(VoteResponse(term = term, voteGranted = false)))
      return
    }

    // If we hear a vote request from a node in a later term, we immediately
    // become a leaderless follower and vote for that node.
    if (voteRequest.term > term) {
      stopTimer(state)
      term = voteRequest.term
      val t = noPingTimer()
      t.start()
      state = LeaderlessFollower(t)
      nodes(src).send(ParticipantInbound().withVoteResponse(VoteResponse(term = term, voteGranted = true)))
      return
    }

    // Otherwise, the vote request is for our current term.
    state match {
      case LeaderlessFollower(noPingTimer) => {
        // We've already voted for a candidate, so we ignore this vote request.
      }
      case Follower(noPingTimer, leader) => {
        // We already have a leader in this term, so there's no need to vote
        // for a leader.
      }
      case Candidate(notEnoughVotesTimer, votes) => {
        // If the vote request is from myself, then I'll vote for myself.
        // Otherwise, I won't vote for another candidate.
        if (src == address) {
          nodes(src).send(ParticipantInbound().withVoteResponse(VoteResponse(term = term, voteGranted = true)))
        }
      }
      case Leader(pingTimer) => {
        // We already have a leader in this term, so there's no need to vote
        // for a leader.
      }
    }
  }

  private def handleVoteResponse(src: Transport#Address, vote: VoteResponse): Unit = {
    // If we hear a vote from an earlier term, we ignore it.
    if (vote.term < term) {
      return
    }

    // Hearing a vote from a future term is impossible! We can't hear a vote
    // in term `r` unless we send a vote request in term `r`. If we're not
    // yet in term `vote.term`, then we never sent a vote request in term
    // `vote.term`.
    if (vote.term > term) {
      logger.fatal(
        s"A node received a vote for term ${vote.term} but is only in " +
          s"term $term."
      )
      return
    }

    state match {
      case LeaderlessFollower(noPingTimer) => {
        // If we're a leaderless follower in this term, then we haven't yet
        // become a candidate in this term. If we haven't yet become a
        // candidate, we never sent a vote request, so we cannot receive a
        // vote.
        logger.fatal(
          s"A node received a vote in term ${vote.term} but is a " +
            "leaderless follower."
        )
        return
      }
      case Follower(noPingTimer, leader) => {
        // It is possible that we were a candidate in this term, then heard
        // from a leader in this term and stepped down to follower. In this
        // case, we simply ignore the vote.
      }
      case Candidate(notEnoughVotesTimer, votes) => {
        val newState = Candidate(notEnoughVotesTimer, votes + src)
        state = newState

        // If we've received votes from a majority of the nodes, then we are
        // the leader for this term. `addresses.size / 2 + 1` is just a
        // formula for a majority.
        if (newState.votes.size >= (addresses.size / 2 + 1)) {
          stopTimer(state)
          val t = pingTimer()
          t.start()
          state = Leader(t)

          for (address <- addresses) {
            nodes(address).send(
              ParticipantInbound().withAppendEntriesRequest(AppendEntriesRequest(term = term, prevLogIndex = getPrevLogIndex(), prevLogTerm = getPrevLogTerm(), entries = List(), leaderCommit = commitIndex))
            )
          }

          callbacks.foreach(_(address))
        }
      }
      case Leader(pingTimer) => {
        // It is possible that a candidate is elected leader and then later
        // receives some votes. We just ignore these votes.
      }
    }
  }

  private def stopTimer(state: ElectionState): Unit = {
    state match {
      case LeaderlessFollower(noPingTimer)   => { noPingTimer.stop() }
      case Follower(noPingTimer, _)          => { noPingTimer.stop() }
      case Candidate(notEnoughVotesTimer, _) => { notEnoughVotesTimer.stop() }
      case Leader(pingTimer)                 => { pingTimer.stop() }
    }
  }

  private def transitionToFollower(
      newterm: Int,
      leader: Transport#Address
  ): Unit = {
    stopTimer(state)
    term = newterm
    val t = noPingTimer()
    t.start()
    state = Follower(t, leader)
    callbacks.foreach(_(leader))
  }

  // Timers ////////////////////////////////////////////////////////////////////
  private def pingTimer(): Transport#Timer = {
    // We make `t` a lazy val to avoid the circular definition.
    lazy val t: Transport#Timer = timer(
      "pingTimer",
      options.pingPeriod,
      () => {
        for (address <- addresses) {
          nodes(address).send(
            ParticipantInbound().withAppendEntriesRequest(AppendEntriesRequest(term = term, prevLogIndex = getPrevLogIndex(), prevLogTerm = getPrevLogTerm(), entries = List(), leaderCommit = commitIndex))
          )
        }
        t.start()
      }
    )
    t
  }

  private def noPingTimer(): Transport#Timer = {
    timer(
      "noPingTimer",
      Util.randomDuration(options.noPingTimeoutMin, options.noPingTimeoutMax),
      () => {
        state match {
          case LeaderlessFollower(noPingTimer) => {
            transitionToCandidate()
          }
          case Follower(noPingTimer, leader) => {
            transitionToCandidate()
          }
          case Candidate(notEnoughVotesTimer, votes) => {
            logger.fatal("A no ping timer was triggered for a candidate!")
          }
          case Leader(pingTimer) => {
            logger.fatal("A no ping timer was triggered for a leader!")
          }
        }
      }
    )
  }

  private def notEnoughVotesTimer(): Transport#Timer = {
    timer(
      "notEnoughVotes",
      Util.randomDuration(
        options.notEnoughVotesTimeoutMin,
        options.notEnoughVotesTimeoutMax
      ),
      () => {
        state match {
          case LeaderlessFollower(noPingTimer) => {
            logger.fatal(
              "A not enough votes timer was triggered for a leaderless " +
                "follower!"
            )
          }
          case Follower(noPingTimer, leader) => {
            logger.fatal(
              "A not enough votes timer was triggered for a follower!"
            )
          }
          case Candidate(notEnoughVotesTimer, votes) => {
            transitionToCandidate()
          }
          case Leader(pingTimer) => {
            logger.fatal("A not enough votes timer was triggered for a leader!")
          }
        }
      }
    )
  }

  private def transitionToCandidate(): Unit = {
    stopTimer(state)
    term += 1
    val t = notEnoughVotesTimer()
    t.start()
    state = Candidate(t, Set())

    for (address <- addresses) {
      nodes(address).send(
        ParticipantInbound().withVoteRequest(VoteRequest(term = term, lastLogIndex = getLastLogIndex(), lastLogTerm = getLastLogTerm()))
      )
    }
  }

  private def getLastLogIndex(): Int = {
    log.length
  }

  private def getLastLogTerm(): Int = {
    if (log.length > 0) {
      log(log.length - 1).term
    } else {
      term - 1
    }
  } 

  private def getPrevLogIndex(): Int = {
    log.length - 1
  }

  private def getPrevLogTerm(): Int = {
    if (log.length > 1) {
      log(log.length - 1).term
    } else {
      -1
    }
  }
}