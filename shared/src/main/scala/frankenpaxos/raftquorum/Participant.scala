package frankenpaxos.raftquorum

import frankenpaxos.raft.{
  AppendEntriesRequest,
  AppendEntriesResponse,
  ClientRequest,
  ClientRequestResponse,
  Command,
  CommandOrNoop,
  ElectionOptions,
  LogEntry,
  Noop,
  VoteRequest,
  VoteResponse
}
import frankenpaxos.{Actor, Chan, Logger, ProtoSerializer, Util}
import frankenpaxos.statemachine.StateMachine
import frankenpaxos.quorums.QuorumSystem
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.scalajs.js.annotation._
import com.google.protobuf.ByteString

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

@JSExportAll
class QuorumParticipant[Transport <: frankenpaxos.Transport[Transport]](
    address: Transport#Address,
    transport: Transport,
    logger: Logger,
    config: Config[Transport],
    val stateMachine: StateMachine,
    val quorumSystem: QuorumSystem[Int],
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
      votes: Set[Int]
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
  log.append(LogEntry(term = 0, command = CommandOrNoop().withNoop(Noop())))

  // The index of highest log entry known to be committed
  var commitIndex: Int = 0

  // The index of highest log entry applied to state machine
  var lastApplied: Int = 0

  // random
  val rand = new Random();

  // Leader State (reinit on election) /////////////////////////////////////////

  // index of next log entry to be sent to participant
  var nextIndex: mutable.Map[Transport#Address, Int] = _

  // index of highest log entry known to be replicated on participant
  var matchIndex: mutable.Map[Transport#Address, Int] = _

  // Helper data structures for Leader (also should reinit on election) //////

  // map of log indexes - client
  var clientWriteReturn: mutable.Map[Int, Chan[QuorumClient[Transport]]] = _

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
        logger.fatal("Empty QuorumParticipantInbound encountered.")
      }
    }
  }

  private def handleVoteRequest(
      src: Transport#Address,
      voteRequest: VoteRequest
  ): Unit = {
    logger.info(
      s"Got VoteRequest from ${src}"
        + s" | Term: ${voteRequest.term}"
        + s" | LastLogIndex = ${voteRequest.lastLogIndex}"
        + s" | LastLogTerm: ${voteRequest.lastLogTerm}"
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
        + s" | Term: ${vote.term}"
        + s" | VoteGranted = ${vote.voteGranted}"
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

        val newState = Candidate(notEnoughVotesTimer, votes + config.participantAddresses.indexOf(src))
        state = newState

        // If we've received votes from a majority of the nodes, then we are
        // the leader for this term. `addresses.size / 2 + 1` is just a
        // formula for a majority.
        if (quorumSystem.isWriteQuorum(newState.votes)) {
          transitionToLeader()
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
        + s" | Command: ${clientRequest.cmd}"
    )

    state match {
      case LeaderlessFollower(_) | Candidate(_, _) => {
        // don't know real leader, so pick a random other node
        clients(src).send(
          QuorumClientInbound().withClientRequestResponse(
            ClientRequestResponse(success = false,
                                  response =
                                    ByteString.copyFromUtf8("NOT_LEADER"),
                                  leaderHint = rand.nextInt(nodes.size)
            )
          )
        )
      }
      case Follower(_, leader) => {
        // we know leader, so send back index of leader
        clients(src).send(
          QuorumClientInbound().withClientRequestResponse(
            ClientRequestResponse(success = false,
                                  response =
                                    ByteString.copyFromUtf8("NOT_LEADER"),
                                  leaderHint = participants.indexOf(leader)
            )
          )
        )
      }
      case Leader(pingTimer) => {
        // add cmd to leader log
        log.append(LogEntry(term = term, command = clientRequest.cmd))

        // keep track of which client is associated with this log entry
        clientWriteReturn.update(getPrevLogIndex(), clients(src))

        // send AppendEntriesRequest to all other participants if possible
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
        + s" | Query: ${clientQuorumQuery.query}"
    )
    val output = stateMachine.run(clientQuorumQuery.query.toByteArray)
    clients(src).send(
      QuorumClientInbound().withClientQuorumQueryResponse(
        ClientQuorumQueryResponse(success = true,
                                  latestIndex = getPrevLogIndex(),
                                  latestCommitted = commitIndex,
                                  response = ByteString.copyFrom(output)
        )
      )
    )
  }

  private def handleAppendEntriesRequest(
      src: Transport#Address,
      appReq: AppendEntriesRequest
  ): Unit = {
    logger.info(
      s"Got AppendEntriesRequest from ${src}"
        + s" | Term: ${appReq.term}"
        + s" | PrevLogIndex: ${appReq.prevLogIndex}"
        + s" | PrevLogTerm: ${appReq.prevLogTerm}"
        + s" | Leader Commit: ${appReq.leaderCommit}"
        + s" | Entries: ${appReq.entries}"
        + s" | UUID: ${appReq.uuid}"
    )
    var success = false
    if (appReq.term > term) {
      // If we hear from a leader in a larger term, then we immediately become a
      // follower of that leader.
      transitionToFollower(appReq.term, src)
    } else if (appReq.term == term) {
      state match {
        case LeaderlessFollower(_) | Candidate(_, _) => {
          transitionToFollower(appReq.term, src)
        }
        case _ =>
      }
      state match {
        case Follower(noPingTimer, leader) => {
          noPingTimer.reset()
          // check that log contains entry at prevLogIndex with term == prevLogTerm
          if (
            appReq.prevLogIndex == 0 ||
            (appReq.prevLogIndex <= log.length &&
            log(appReq.prevLogIndex).term == appReq.prevLogTerm)
          ) {
            success = true
            // prune conflicting entries
            val start = appReq.prevLogIndex + 1
            val entries = appReq.entries
            for (i <- start until log.length) {
              if (log(i).term != entries(i - start).term) {
                logger.info(s"Removing index and beyond: ${i}")
                log.remove(i, log.length - i)
              }
            }
            // apply new entries
            for (i <- 0 until entries.length) {
              if ((i + start) >= log.length) {
                logger.info(s"Appending ${entries(i)} at ${i + start}")
                log.append(entries(i))
              }
            }
            // update commit index
            val oldCommitIndex = commitIndex
            if (appReq.leaderCommit > commitIndex) {
              commitIndex = appReq.leaderCommit.min(getPrevLogIndex())
            }
            // execute commands on state machine
            for (index <- oldCommitIndex + 1 to commitIndex) {
              logger.info(s"Commiting index: ${index}")
              applyCommandOrNoop(log(index).command)
            }
          }
        }
        case default => {
          logger.info(s"${default} recieved an AppendEntriesRequest.")
        }
      }
    } else {
      logger.error(
        s"Recieved an AppendEntriesRequest with term ${appReq.term} < ${term}"
      )
    }
    // send response
    val response = AppendEntriesResponse(term = term,
                                         success = success,
                                         lastLogIndex = getPrevLogIndex(),
                                         uuid = appReq.uuid
    )
    logger.info(
      s"Sending AppendEntriesResponse to ${src}"
        + s" | Term: ${response.term}"
        + s" | Success = ${response.success}"
        + s" | LastLogIndex = ${response.lastLogIndex}"
        + s" | UUID: ${response.uuid}"
    )
    nodes(src).send(
      QuorumParticipantInbound().withAppendEntriesResponse(response)
    )
  }

  private def handleAppendEntriesResponse(
      src: Transport#Address,
      appRes: AppendEntriesResponse
  ): Unit = {
    logger.info(
      s"Got AppendEntriesResponse from ${src}"
        + s" | Term: ${appRes.term}"
        + s" | Success: ${appRes.success}"
        + s" | LastLogIndex: ${appRes.lastLogIndex}"
        + s" | UUID: ${appRes.uuid}"
    )
    // If we hear from a leader in a larger term, then we immediately become a follower.
    if (appRes.term > term) {
      transitionToFollower(appRes.term, src)
    } else if (appRes.term == term) {
      state match {
        case Leader(_) => {
          if (appRes.success) {
            // Update nextIndex and matchIndex for follower (src)
            matchIndex.update(src, matchIndex(src).max(appRes.lastLogIndex))
            nextIndex.update(src, matchIndex(src) + 1)
            // Commit entries
            // If there exists an N such that N > commitIndex, a majority
            // of matchIndex[i] ≥ N, and log[N].term == currentTerm:
            // set commitIndex = N
            val oldCommitIndex = commitIndex
            for ((addr1, index1) <- matchIndex) {
              var count: Set[Int] = Set()
              count += config.participantAddresses.indexOf(address)
              for ((addr2, index2) <- matchIndex) {
                if (index2 >= index1) {
                  count += config.participantAddresses.indexOf(addr2)
                }
              }
              if (
                quorumSystem.isWriteQuorum(count) && log(
                  index1
                ).term == term
              ) {
                commitIndex = commitIndex.max(index1)
              }
            }
            // Process new committed entries one by one
            for (index <- oldCommitIndex + 1 to commitIndex) {
              logger.info(s"Committing index ${index}")
              // Execute command on state machine
              val output = applyCommandOrNoop(log(index).command)
              // send successful responses to clients whose command for that index have been committed
              if (clientWriteReturn.contains(index)) {
                clientWriteReturn(index).send(
                  QuorumClientInbound().withClientRequestResponse(
                    ClientRequestResponse(success = true,
                                          response =
                                            ByteString.copyFrom(output),
                                          leaderHint =
                                            participants.indexOf(leader)
                    )
                  )
                )
                clientWriteReturn -= index
              }
            }
          } else {
            // decrement nextIndex for follower (src) and retry AppendEntriesRequest
            nextIndex.update(src, 1.max(nextIndex(src) - 1))
            sendAppEntReq(src)
          }
        }
        case default => {
          logger.info(s"${default} recieved an AppendEntriesResponse.")
        }
      }
    } else {
      logger.error(
        s"Recieved an AppendEntriesResponse with term ${appRes.term} < ${term}"
      )
    }
  }

  // Timers ////////////////////////////////////////////////////////////////////

  private def stopTimer(state: ElectionState): Unit = {
    state match {
      case LeaderlessFollower(noPingTimer)   => { noPingTimer.stop() }
      case Follower(noPingTimer, _)          => { noPingTimer.stop() }
      case Candidate(notEnoughVotesTimer, _) => { notEnoughVotesTimer.stop() }
      case Leader(pingTimer)                 => { pingTimer.stop() }
    }
  }

  private def pingTimer(): Transport#Timer = {
    // We make `t` a lazy val to avoid the circular definition.
    lazy val t: Transport#Timer = timer(
      "pingTimer",
      options.pingPeriod,
      () => {
        for (addr <- participants) {
          if (!addr.equals(address)) {
            sendAppEntReq(addr)
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
          case LeaderlessFollower(_) | Follower(_, _) => {
            transitionToCandidate()
          }
          case Candidate(_, _) | Leader(_) => {
            logger.fatal(s"A no ping timer was triggered for ${state}")
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
          case Candidate(_, _) => {
            transitionToCandidate()
          }
          case LeaderlessFollower(_) | Follower(_, _) | Leader(_) => {
            logger.fatal(s"A not enough votes timer was triggered for ${state}")
          }
        }
      }
    )
  }

  // Node State Transitions  ////////////////////////////////////////////////////

  private def transitionToLeader(): Unit = {
    logger.info(s"Transitioning to leader from ${state}")
    stopTimer(state)
    val t = pingTimer()
    t.start()
    state = Leader(t)
    nextIndex = mutable.Map[Transport#Address, Int]()
    config.participantAddresses.foreach { a =>
      nextIndex.update(a, getPrevLogIndex() + 1)
    }
    matchIndex = mutable.Map[Transport#Address, Int]()
    config.participantAddresses.foreach { a => matchIndex.update(a, 0) }
    clientWriteReturn = mutable.Map[Int, Chan[QuorumClient[Transport]]]()

    log.append(
      LogEntry(term = term, command = CommandOrNoop().withNoop(Noop()))
    )
    for (addr <- participants) {
      if (!addr.equals(address)) {
        sendAppEntReq(addr)
      }
    }
  }

  private def transitionToFollower(
      newterm: Int,
      leader: Transport#Address
  ): Unit = {
    logger.info(s"Transitioning to follower from ${state}")
    stopTimer(state)
    term = newterm
    val t = noPingTimer()
    t.start()
    state = Follower(t, leader)
  }

  private def transitionToCandidate(): Unit = {
    logger.info(s"Transitioning to candidate from ${state}")
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
                      lastLogTerm = getPrevLogTerm()
          )
        )
      )
    }
  }

  // Helpers /////////////////////////////////////////////////////////////////

  private def getPrevLogIndex(): Int = {
    log.length - 1
  }

  private def getPrevLogTerm(): Int = {
    // log always has length >= 1
    log(log.length - 1).term
  }

  private def sendAppEntReq(
      address: Transport#Address,
      uuid: Option[Int] = None
  ): Unit = {
    val prevLogIndex = nextIndex(address) - 1
    val request = AppendEntriesRequest(
      term = term,
      prevLogIndex = prevLogIndex,
      prevLogTerm = log(prevLogIndex).term,
      entries = log.slice(prevLogIndex + 1, log.length),
      leaderCommit = commitIndex.min(log.length),
      uuid = uuid
    )
    logger.info(
      s"Sending AppendEntriesRequest to ${address}"
        + s" | Term: ${request.term}"
        + s" | PrevLogIndex = ${request.prevLogIndex}"
        + s" | PrevLogTerm = ${request.prevLogTerm}"
        + s" | Entries = ${request.entries}"
        + s" | Leader Commit = ${request.leaderCommit}"
        + s" | UUID = ${request.uuid}"
    )
    nodes(address).send(
      QuorumParticipantInbound().withAppendEntriesRequest(request)
    )
  }

  private def applyCommandOrNoop(commandOrNoop: CommandOrNoop): Array[Byte] = {
    import CommandOrNoop.Value
    commandOrNoop.value match {
      case Value.Command(command) =>
        return stateMachine.run(command.cmd.toByteArray)
      case Value.Noop(_) =>
        return Array[Byte]()
      case Value.Empty =>
        logger.fatal("Empty CommandOrNoop.")
    }
  }
}