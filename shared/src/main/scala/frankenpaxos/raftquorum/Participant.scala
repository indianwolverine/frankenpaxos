package frankenpaxos.raftquorum

import frankenpaxos.raft.{LogEntry, 
                          ClientRequest, 
                          AppendEntriesRequest, 
                          AppendEntriesResponse, 
                          VoteRequest, 
                          VoteResponse, 
                          ClientRequestResponse}

import frankenpaxos.Actor
import frankenpaxos.Chan
import frankenpaxos.Logger
import frankenpaxos.ProtoSerializer
import frankenpaxos.Util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.scalajs.js.annotation._

@JSExportAll
object QuorumParticipantInboundSerializer
    extends ProtoSerializer[QuorumParticipantInbound] {
  type A = QuorumParticipantInbound
  override def toBytes(x: A): Array[Byte] = super.toBytes(x)
  override def fromBytes(bytes: Array[Byte]): A = super.fromBytes(bytes)
  override def toPrettyString(x: A): String = super.toPrettyString(x)
}

@JSExportAll
object QuorumParticipant {
  val serializer = QuorumParticipantInboundSerializer
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
class QuorumParticipant[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport],
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
  override type InboundMessage = QuorumParticipantInbound
  override def serializer = QuorumParticipant.serializer

  // Sanity check arguments.
  logger.check(config.participantAddresses.contains(address))
  logger.checkLe(options.noPingTimeoutMin, options.noPingTimeoutMax)
  logger.checkLe(options.notEnoughVotesTimeoutMin,
                 options.notEnoughVotesTimeoutMax
  )
  leader match {
    case Some(address) =>
      logger.check(config.participantAddresses.contains(address))
    case None =>
  }

  // The set of participant nodes in a Seq.
  // Indices of this list are used to communicate leader information to clients.
  val participants: Seq[Transport#Address] = {
    for (participantAddress <- config.participantAddresses)
      yield participantAddress
  }

  // The addresses of the other participants.
  val nodes: Map[Transport#Address, Chan[QuorumParticipant[Transport]]] = {
    for (participantAddress <- config.participantAddresses)
      yield (participantAddress ->
        chan[QuorumParticipant[Transport]](participantAddress,
                                     QuorumParticipant.serializer
        ))
  }.toMap

  // The addresses of the clients.
  val clients: Map[Transport#Address, Chan[QuorumClient[Transport]]] = {
    for (clientAddress <- config.clientAddresses)
      yield (clientAddress ->
        chan[QuorumClient[Transport]](clientAddress, QuorumClient.serializer))
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
  var log: ArrayBuffer[LogEntry] = new ArrayBuffer[LogEntry](0)
  // add dummy entry to log
  log.append(LogEntry(term = 0, command = "dummy"))

  // The index of highest log entry known to be committed
  var commitIndex: Int = 0

  // The index of highest log entry applied to state machine
  var lastApplied: Int = 0

  // Leader State (reinit on election) /////////////////////////////////////////

  // index of next log entry to be sent to participant
  var nextIndex: mutable.Map[Transport#Address, Int] =
    mutable.Map[Transport#Address, Int]()
  config.participantAddresses.foreach { a => nextIndex.update(a, 1) }

  // index of highest log entry known to be replicated on participant
  var matchIndex: mutable.Map[Transport#Address, Int] =
    mutable.Map[Transport#Address, Int]()
  config.participantAddresses.foreach { a => matchIndex.update(a, 1) }

  // map of clients whose commands are at log index n
  var clientWriteReturn: mutable.Map[Int, Chan[QuorumClient[Transport]]] =
    mutable.Map[Int, Chan[QuorumClient[Transport]]]()

  // map of clients with read requests for index n
  var clientReadReturn: mutable.Map[Int, ArrayBuffer[Chan[QuorumClient[Transport]]]] =
    mutable.Map[Int, ArrayBuffer[Chan[QuorumClient[Transport]]]]()

  // random
  val rand = new Random();

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
    import QuorumParticipantInbound.Request
    inbound.request match {
      case Request.ClientRequest(r)        => handleClientRequest(src, r)
      case Request.ClientQuorumQuery(r)    => handleClientQuorumQuery(src, r)
      case Request.AppendEntriesRequest(r) => handleAppendEntriesRequest(src, r)
      case Request.AppendEntriesResponse(r) =>
        handleAppendEntriesResponse(src, r)
      case Request.VoteRequest(r)  => handleVoteRequest(src, r)
      case Request.VoteResponse(r) => handleVoteResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty LeaderInbound encountered.")
      }
    }
  }

  private def handleVoteRequest(
      src: Transport#Address,
      voteRequest: VoteRequest
  ): Unit = {
    logger.info(
      s"Got VoteRequest from ${src}"
        + s"| Term: ${voteRequest.term}"
        + s"| LastLogIndex = ${voteRequest.lastLogIndex}"
        + s"| LastLogTerm: ${voteRequest.lastLogTerm}"
    )

    // If we hear a vote request from an earlier term, reply with current term and don't grant vote.
    if (voteRequest.term < term) {
      nodes(src).send(
        QuorumParticipantInbound().withVoteResponse(
          VoteResponse(term = term, voteGranted = false)
        )
      )
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
      nodes(src).send(
        QuorumParticipantInbound().withVoteResponse(
          VoteResponse(term = term, voteGranted = true)
        )
      )
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
          nodes(src).send(
            QuorumParticipantInbound().withVoteResponse(
              VoteResponse(term = term, voteGranted = true)
            )
          )
        }
      }
      case Leader(pingTimer) => {
        // We already have a leader in this term, so there's no need to vote
        // for a leader.
      }
    }
  }

  private def handleVoteResponse(
      src: Transport#Address,
      vote: VoteResponse
  ): Unit = {
    logger.info(
      s"Got VoteResponse from ${src}"
        + s"| Term: ${vote.term}"
        + s"| VoteGranted = ${vote.voteGranted}"
    )

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
        // exit early if vote is not granted
        if (!vote.voteGranted) {
          return
        }

        val newState = Candidate(notEnoughVotesTimer, votes + src)
        state = newState

        // If we've received votes from a majority of the nodes, then we are
        // the leader for this term. `addresses.size / 2 + 1` is just a
        // formula for a majority.
        if (newState.votes.size >= (participants.size / 2 + 1)) {
          stopTimer(state)
          val t = pingTimer()
          t.start()
          state = Leader(t)

          for (addr <- participants) {
            if (!addr.equals(address)) {
              nodes(addr).send(
                QuorumParticipantInbound().withAppendEntriesRequest(
                  AppendEntriesRequest(
                    term = term,
                    prevLogIndex = getPrevLogIndex(),
                    prevLogTerm = getPrevLogTerm(),
                    entries = List(LogEntry(term = term, command = "noop")),
                    leaderCommit = commitIndex
                  )
                )
              )
            }
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

  private def handleClientRequest(
      src: Transport#Address,
      clientRequest: ClientRequest
  ): Unit = {
    logger.info(
      s"Got ClientRequest from ${src}"
        + s"| Command: ${clientRequest.cmd}"
    )

    state match {
      case LeaderlessFollower(_) | Candidate(_, _) => {
        // don't know real leader, so pick a random other node
        clients(src).send(
          QuorumClientInbound().withClientRequestResponse(
            ClientRequestResponse(success = false,
                                  response = "NOT_LEADER",
                                  leaderHint = rand.nextInt(nodes.size)
            )
          )
        )
      }
      case Follower(noPingTimer, leader) => {
        // we know leader, so send back index of leader
        val leaderIndex = participants.indexOf(leader)
        clients(src).send(
          QuorumClientInbound().withClientRequestResponse(
            ClientRequestResponse(success = false,
                                  response = "NOT_LEADER",
                                  leaderHint = leaderIndex
            )
          )
        )
      }
      case Leader(pingTimer) => {
        // leader can handle client requests directly

        // add cmd to leader log
        log.append(LogEntry(term = term, command = clientRequest.cmd))

        // keep track of which client is associated with this log entry
        clientWriteReturn.update(getPrevLogIndex(), clients(src))

        // send AppendEntriesRequest to all participants
        for (addr <- participants) {
          if (!addr.equals(address)) {
            sendAppEntReq(addr)
          }
        }
      }
    }
  }

  private def handleClientQuorumQuery(
      src: Transport#Address,
      clientQuorumQuery: ClientQuorumQuery
  ): Unit = {
    logger.info(
      s"Got ClientQuorumQuery from ${src}"
        + s"| Index: ${clientQuorumQuery.index}"
    )

    // state match {
    //   case LeaderlessFollower(_) | Candidate(_, _) => {
    //     // don't know real leader, so pick a random other node
    //     clients(src).send(
    //       QuorumClientInbound().withClientQuorumQueryResponse(
    //         ClientQuorumQueryResponse(success = false,
    //                                   response = "NOT_LEADER",
    //                                   leaderHint = rand.nextInt(nodes.size)
    //         )
    //       )
    //     )
    //   }
    //   case Follower(noPingTimer, leader) => {
    //     // we know leader, so send back index of leader
    //     val leaderIndex = participants.indexOf(leader)
    //     clients(src).send(
    //       QuorumClientInbound().withClientQuorumQueryResponse(
    //         ClientQuorumQueryResponse(success = false,
    //                             response = "NOT_LEADER",
    //                             leaderHint = leaderIndex
    //         )
    //       )
    //     )
    //   }
    //   case Leader(pingTimer) => {
    //     // leader can handle client requests directly

    //     // command is committed in log, return immediately
    //     if (clientQuorumQuery.index <= commitIndex) {
    //       val leaderIndex = participants.indexOf(leader)
    //       clients(src).send(
    //         QuorumClientInbound().withClientQuorumQueryResponse(
    //           ClientQuorumQueryResponse(success = true,
    //                               response = log(clientQuery.index).command,
    //                               leaderHint = leaderIndex
    //           )
    //         )
    //       )
    //     } else {
    //       // otherwise, keep this request around and service it when index commits
    //       clientReadReturn get clientQuorumQuery.index match {
    //         case Some(res) =>
    //           clientReadReturn(clientQuery.index).append(clients(src))
    //         case None => {
    //           var clientReads: ArrayBuffer[Chan[QuorumClient[Transport]]] =
    //             new ArrayBuffer[Chan[QuorumClient[Transport]]](0)
    //           clientReads.append(clients(src))
    //           clientReadReturn.update(clientQuorumQuery.index, clientReads)
    //         }
    //       }
    //     }
    //   }
    // }
  }

  private def handleAppendEntriesRequest(
      src: Transport#Address,
      appReq: AppendEntriesRequest
  ): Unit = {
    logger.info(
      s"Got AppendEntriesRequest from ${src}"
        + s"| Term: ${appReq.term}"
        + s"| PrevLogIndex = ${appReq.prevLogIndex}"
        + s"| PrevLogTerm: ${appReq.prevLogTerm}"
        + s"| Leader Commit: ${appReq.leaderCommit}"
        + s"| Entries: ${appReq.entries}"
    )

    // If we hear a ping from an earlier term, return false and term.
    if (appReq.term < term) {
      nodes(src).send(
        QuorumParticipantInbound().withAppendEntriesResponse(
          AppendEntriesResponse(term = term,
                                success = false,
                                lastLogIndex = getPrevLogIndex()
          )
        )
      )
      return
    }

    // If we hear from a leader in a larger term, then we immediately become a
    // follower of that leader.
    if (appReq.term > term) {
      transitionToFollower(appReq.term, src)
      return
    }

    state match {
      case LeaderlessFollower(noPingTimer) => {
        transitionToFollower(appReq.term, src)
      }
      case Follower(noPingTimer, leader) => {
        // reset heartbeat timer
        noPingTimer.reset()

        // If leaderCommit > commitIndex, set commitIndex =
        // min(leaderCommit, index of last new entry)
        if (appReq.leaderCommit > commitIndex) {
          commitIndex = appReq.leaderCommit.min(getPrevLogIndex())
        }

        // if this is not a hearbeat msg, main appendEntries logic
        if (appReq.entries.length > 0) {
          // check that log contains entry at prevLogIndex with term == prevLogTerm
          if (!checkPrevEntry(appReq.prevLogIndex, appReq.prevLogIndex)) {
            nodes(src).send(
              QuorumParticipantInbound().withAppendEntriesResponse(
                AppendEntriesResponse(term = term,
                                      success = false,
                                      lastLogIndex = getPrevLogIndex()
                )
              )
            )
            return
          }

          // Prune conflicting entries and
          // append any new entries not already in the log
          applyEntries(appReq.prevLogIndex + 1, appReq.entries)

          // send success response
          nodes(src).send(
            QuorumParticipantInbound().withAppendEntriesResponse(
              AppendEntriesResponse(term = term,
                                    success = true,
                                    lastLogIndex = getPrevLogIndex()
              )
            )
          )
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

  private def handleAppendEntriesResponse(
      src: Transport#Address,
      appRes: AppendEntriesResponse
  ): Unit = {
    logger.info(
      s"Got AppendEntriesResponse from ${src}"
        + s"| Term: ${appRes.term}"
        + s"| Success = ${appRes.success}"
        + s"| LastLogIndex = ${appRes.lastLogIndex}"
    )

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
          // Update nextIndex and matchIndex for follower (src)
          nextIndex.update(src, appRes.lastLogIndex + 1)
          matchIndex.update(src, appRes.lastLogIndex)

          // If there exists an N such that N > commitIndex, a majority
          // of matchIndex[i] ≥ N, and log[N].term == currentTerm:
          // set commitIndex = N
          commitIndex = updateCommitIndex()

          // send successful responses to clients whose commands have been committed
          val leaderIndex = participants.indexOf(leader)

          // service pending write requests
          for ((index, client) <- clientWriteReturn) {
            if (commitIndex >= index) {
              clientWriteReturn(index).send(
                QuorumClientInbound().withClientRequestResponse(
                  ClientRequestResponse(success = true,
                                        response = "OK",
                                        leaderHint = leaderIndex
                  )
                )
              )
              clientWriteReturn.remove(index)
            }
          }

          // service pending read requests
          // for ((index, client) <- clientReadReturn) {
          //   if (commitIndex >= index) {
          //     for (addr <- clientReadReturn(index)) {
          //       addr.send(
          //         QuorumClientInbound().withClientQueryResponse(
          //           ClientQueryResponse(success = true,
          //                               response = log(index).command,
          //                               leaderHint = leaderIndex
          //           )
          //         )
          //       )
          //     }
          //     clientReadReturn.remove(index)
          //   }
          // }
        } else {
          // decrement nextIndex for follower (src) and retry AppendEntriesRequest
          nextIndex.update(src, nextIndex(src) - 1)
          sendAppEntReq(src)
        }
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
        for (addr <- participants) {
          if (!addr.equals(address)) {
            nodes(addr).send(
              QuorumParticipantInbound().withAppendEntriesRequest(
                AppendEntriesRequest(term = term,
                                     prevLogIndex = getPrevLogIndex(),
                                     prevLogTerm = getPrevLogTerm(),
                                     entries = List(),
                                     leaderCommit = commitIndex
                )
              )
            )
          }
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

  // Helpers /////////////////////////////////////////////////////////////////
  private def transitionToCandidate(): Unit = {
    stopTimer(state)
    term += 1
    val t = notEnoughVotesTimer()
    t.start()
    state = Candidate(t, Set())

    for (address <- participants) {
      nodes(address).send(
        QuorumParticipantInbound().withVoteRequest(
          VoteRequest(term = term,
                      lastLogIndex = getPrevLogIndex(),
                      lastLogTerm = getLastLogTerm()
          )
        )
      )
    }
  }

  private def getLastLogIndex(): Int = {
    log.length
  }

  private def getLastLogTerm(): Int = {
    if (log.length > 0) {
      return log(log.length - 1).term
    } else {
      return term - 1
    }
  }

  private def getPrevLogIndex(): Int = {
    // log always has length >= 1
    log.length - 1
  }

  private def getPrevLogTerm(): Int = {
    // log always has length >= 1
    log(log.length - 1).term
  }

  private def checkPrevEntry(prevLogIndex: Int, prevLogTerm: Int): Boolean = {
    if (prevLogIndex < log.length) {
      return log(prevLogIndex).term == prevLogTerm
    }
    false
  }

  private def applyEntries(start: Int, entries: Seq[LogEntry]): Unit = {
    // prune conflicting entries
    for (i <- start until log.length) {
      if (log(i).term != entries(i - start).term) {
        log.remove(i, log.length - i)
      }
    }

    // apply new entries
    for (i <- 0 until entries.length) {
      if ((i + start) >= log.length) {
        log.append(entries(i))
      }
    }
  }

  private def sendAppEntReq(address: Transport#Address): Unit = {
    logger.info(s"Sending AppendEntriesRequest to ${address}")

    val prevLogIndex = nextIndex(address) - 1
    val prevLogTerm = log(prevLogIndex).term

    var entries: ArrayBuffer[LogEntry] = new ArrayBuffer[LogEntry](0)

    for (i <- nextIndex(address) until log.length) {
      entries.append(log(i))
    }

    nodes(address).send(
      QuorumParticipantInbound().withAppendEntriesRequest(
        AppendEntriesRequest(term = term,
                             prevLogIndex = prevLogIndex,
                             prevLogTerm = prevLogTerm,
                             entries = entries,
                             leaderCommit = commitIndex
        )
      )
    )
  }

  private def updateCommitIndex(): Int = {
    var maxMajorityIndex: Int = commitIndex

    for ((addr1, index1) <- matchIndex) {
      var count: Int = 0
      for ((addr2, index2) <- matchIndex) {
        if (index2 >= index1) {
          count += 1
        }
      }
      if (count > ((participants.size / 2) + 1) && log(index1).term == term) {
        maxMajorityIndex = maxMajorityIndex.max(index1)
      }
    }

    return maxMajorityIndex
  }
}
