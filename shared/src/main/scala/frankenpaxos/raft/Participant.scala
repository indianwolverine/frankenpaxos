package frankenpaxos.raft

import frankenpaxos.{Actor, Chan, Logger, ProtoSerializer, Util}
import frankenpaxos.statemachine.StateMachine
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.scalajs.js.annotation._
import com.google.protobuf.ByteString

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
    pingPeriod = java.time.Duration.ofSeconds(5),
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
    config: Config[Transport],
    val stateMachine: StateMachine,
    leader: Option[Transport#Address] = None,
    options: ElectionOptions = ElectionOptions.default,
    val participantIndex: Int
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
  override type InboundMessage = ParticipantInbound
  override def serializer = Participant.serializer

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
  val nodes: Map[Int, Chan[Participant[Transport]]] = {
    for (i <- 0 until config.participantAddresses.size)
      yield (i ->
        chan[Participant[Transport]](config.participantAddresses(i),
                                     Participant.serializer
        ))
  }.toMap

  // The addresses of the clients.
  val clients: Map[Int, Chan[Client[Transport]]] = {
    for (i <- 0 until config.clientAddresses.size)
      yield (i ->
        chan[Client[Transport]](config.clientAddresses(i), Client.serializer))
  }.toMap

  // The current term.
  var term: Int = 0

  // The current state.
  var state: ElectionState = {
    leader match {
      case Some(leaderAddress) =>
        if (address == leaderAddress) {
          transitionToLeader()
        } else {
          transitionToFollower(0, leaderAddress)
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
  var nextIndex: mutable.Map[Int, Int] = mutable.Map[Int, Int]()
  (0 until config.participantAddresses.size).foreach { a =>
      nextIndex.update(a, getPrevLogIndex() + 1)
  }

  // index of highest log entry known to be replicated on participant
  var matchIndex: mutable.Map[Int, Int] = mutable.Map[Int, Int]()
  (0 until config.participantAddresses.size).foreach { a =>
      matchIndex.update(a, getPrevLogIndex() + 1)
  }

  // Helper data structures for Leader (also should reinit on election) //////

  // map of log indexes - client
  var clientWriteReturn: mutable.Map[Int, Chan[Client[Transport]]] = mutable.Map[Int, Chan[Client[Transport]]]()

  // map tracking read heartbeats: uuid -> count, command, clientsrc
  var clientReads
      : mutable.Map[Int, Tuple3[Int, ReadCommand, Chan[Client[Transport]]]] =
    mutable.Map[Int, Tuple3[Int, ReadCommand, Chan[Client[Transport]]]]()

  // To identify heartbeat messages
  var uuid: Int = _

  // Receive ///////////////////////////////////////////////////////////////////
  override def receive(
      src: Transport#Address,
      inbound: InboundMessage
  ): Unit = {
    import ParticipantInbound.Request
    inbound.request match {
      case Request.ClientRequest(r)        => handleClientRequest(src, r)
      case Request.ClientQuery(r)          => handleClientQuery(src, r)
      case Request.AppendEntriesRequest(r) => handleAppendEntriesRequest(src, r)
      case Request.AppendEntriesResponse(r) =>
        handleAppendEntriesResponse(src, r)
      case Request.VoteRequest(r)  => handleVoteRequest(src, r)
      case Request.VoteResponse(r) => handleVoteResponse(src, r)
      case Request.Empty => {
        logger.fatal("Empty ParticipantInbound encountered.")
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
        + s" | ParticipantIndex: ${voteRequest.participantIndex}"
    )

    // If we hear a vote request from an earlier term, reply with current term and don't grant vote.
    if (voteRequest.term < term) {
      nodes(voteRequest.participantIndex).send(
        ParticipantInbound().withVoteResponse(
          VoteResponse(term = term, voteGranted = false, participantIndex = participantIndex)
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
      nodes(voteRequest.participantIndex).send(
        ParticipantInbound().withVoteResponse(
          VoteResponse(term = term, voteGranted = true, participantIndex = participantIndex)
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
        if (voteRequest.participantIndex == participantIndex) {
          nodes(voteRequest.participantIndex).send(
            ParticipantInbound().withVoteResponse(
              VoteResponse(term = term, voteGranted = true, participantIndex = participantIndex)
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
        + s" | ParticipantIndex: ${vote.participantIndex}"
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

        val newState = Candidate(notEnoughVotesTimer, votes + vote.participantIndex)
        state = newState

        // If we've received votes from a majority of the nodes, then we are
        // the leader for this term. `addresses.size / 2 + 1` is just a
        // formula for a majority.
        if (newState.votes.size >= (participants.size / 2 + 1)) {
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

    // send back to client channel
    val client = chan[Client[Transport]](src, Client.serializer)

    state match {
      case LeaderlessFollower(_) | Candidate(_, _) => {
        // don't know real leader, so pick a random other node
        client.send(
          ClientInbound().withClientRequestResponse(
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
        client.send(
          ClientInbound().withClientRequestResponse(
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
        clientWriteReturn.update(getPrevLogIndex(), client)

        // send AppendEntriesRequest to all other participants if possible
        for (index <- 0 until participants.size) {
          if (!participants(index).equals(address)) {
            sendAppEntReq(index)
          }
        }
      }
    }
  }

  private def handleClientQuery(
      src: Transport#Address,
      clientQuery: ClientQuery
  ): Unit = {
    val readQuery = clientQuery.query
    logger.info(
      s"Got ClientQuery from ${src}"
        + s" | Query: ${readQuery.query}"
    )

    // send back to client channel
    val client = chan[Client[Transport]](src, Client.serializer)

    state match {
      case LeaderlessFollower(_) | Candidate(_, _) => {
        // don't know real leader, so pick a random other node
        client.send(
          ClientInbound().withClientQueryResponse(
            ClientQueryResponse(success = false,
                                response =
                                  ByteString.copyFromUtf8("NOT_LEADER"),
                                leaderHint = rand.nextInt(nodes.size)
            )
          )
        )
      }
      case Follower(noPingTimer, leader) => {
        // we know leader, so send back index of leader
        client.send(
          ClientInbound().withClientQueryResponse(
            ClientQueryResponse(success = false,
                                response =
                                  ByteString.copyFromUtf8("NOT_LEADER"),
                                leaderHint = participants.indexOf(leader)
            )
          )
        )
      }
      case Leader(pingTimer) => {
        // Bump uuid to identify this heartbeat
        uuid += 1
        clientReads(uuid) = Tuple3(0, readQuery, client)

        // send heartbeats to all other participants
        for (index <- 0 until participants.size) {
          if (!participants(index).equals(address)) {
            sendAppEntReq(index, Some(uuid))
          }
        }
      }
    }
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
        + s" | ParticipantIndex: ${appReq.participantIndex}"
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
                                         uuid = appReq.uuid,
                                         participantIndex = participantIndex
    )
    logger.info(
      s"Sending AppendEntriesResponse to ${src}"
        + s" | Term: ${response.term}"
        + s" | Success = ${response.success}"
        + s" | LastLogIndex = ${response.lastLogIndex}"
        + s" | UUID: ${response.uuid}"
        + s" | ParticipantIndex: ${response.participantIndex}"
    )
    nodes(appReq.participantIndex).send(ParticipantInbound().withAppendEntriesResponse(response))
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
        + s" | ParticipantIndex: ${appRes.participantIndex}"
    )
    // If we hear from a leader in a larger term, then we immediately become a follower.
    if (appRes.term > term) {
      transitionToFollower(appRes.term, src)
    } else if (appRes.term == term) {
      state match {
        case Leader(_) => {
          if (appRes.success) {
            // Update nextIndex and matchIndex for follower (src)
            matchIndex.update(appRes.participantIndex, matchIndex(appRes.participantIndex).max(appRes.lastLogIndex))
            nextIndex.update(appRes.participantIndex, matchIndex(appRes.participantIndex) + 1)
            // Commit entries
            // If there exists an N such that N > commitIndex, a majority
            // of matchIndex[i] ≥ N, and log[N].term == currentTerm:
            // set commitIndex = N
            val oldCommitIndex = commitIndex
            for ((addr1, index1) <- matchIndex) {
              var count: Int = 0
              for ((addr2, index2) <- matchIndex) {
                if (index2 >= index1) {
                  count += 1
                }
              }
              if (
                // No + 1 here because the leader has the longest log
                count >= ((participants.size / 2)) && log(
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
                  ClientInbound().withClientRequestResponse(
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
            // Handle read majority if reached
            appRes.uuid match {
              case Some(uuid) => {
                if (clientReads.contains(uuid)) {
                  val tuple = clientReads(uuid)
                  val count = tuple._1 + 1
                  clientReads(uuid) = Tuple3(count, tuple._2, tuple._3)
                  // No + 1 here because the leader is part of the majority
                  if (count >= (participants.size / 2)) {
                    logger.info(s"Heartbeat majority reached for ${uuid}")
                    val output = stateMachine.run(tuple._2.toByteArray)
                    tuple._3.send(
                      ClientInbound().withClientQueryResponse(
                        ClientQueryResponse(success = true,
                                            response =
                                              ByteString.copyFrom(output),
                                            leaderHint =
                                              participants.indexOf(leader)
                        )
                      )
                    )
                    clientReads -= uuid
                  }
                }
              }
              case None =>
            }
          } else {
            // decrement nextIndex for follower (src) and retry AppendEntriesRequest
            nextIndex.update(appRes.participantIndex, 1.max(nextIndex(appRes.participantIndex) - 1))
            sendAppEntReq(appRes.participantIndex)
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
        for (index <- 0 until participants.size) {
          if (!participants(index).equals(address)) {
            sendAppEntReq(index)
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

  private def transitionToLeader(): ElectionState = {
    logger.info(s"Transitioning to leader from ${state}")
    stopTimer(state)
    val t = pingTimer()
    t.start()
    state = Leader(t)
    nextIndex = mutable.Map[Int, Int]()
    (0 until config.participantAddresses.size).foreach { a =>
      nextIndex.update(a, getPrevLogIndex() + 1)
    }
    matchIndex = mutable.Map[Int, Int]()
    (0 until config.participantAddresses.size).foreach { a => matchIndex.update(a, 0) }
    clientWriteReturn = mutable.Map[Int, Chan[Client[Transport]]]()
    clientReads =
      mutable.Map[Int, Tuple3[Int, ReadCommand, Chan[Client[Transport]]]]()
    uuid = 0

    log.append(
      LogEntry(term = term, command = CommandOrNoop().withNoop(Noop()))
    )
    for (index <- 0 until participants.size) {
      if (!participants(index).equals(address)) {
        sendAppEntReq(index)
      }
    }
    state
  }

  private def transitionToFollower(
      newterm: Int,
      leader: Transport#Address
  ): ElectionState = {
    logger.info(s"Transitioning to follower from ${state}")
    stopTimer(state)
    term = newterm
    val t = noPingTimer()
    t.start()
    state = Follower(t, leader)
    state
  }

  private def transitionToCandidate(): ElectionState = {
    logger.info(s"Transitioning to candidate from ${state}")
    stopTimer(state)
    term += 1
    val t = notEnoughVotesTimer()
    t.start()
    state = Candidate(t, Set())

    for (index <- 0 until participants.size) {
      nodes(index).send(
        ParticipantInbound().withVoteRequest(
          VoteRequest(term = term,
                      lastLogIndex = getPrevLogIndex(),
                      lastLogTerm = getPrevLogTerm(),
                      participantIndex = participantIndex
          )
        )
      )
    }
    state
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
      index: Int,
      uuid: Option[Int] = None
  ): Unit = {
    val prevLogIndex = nextIndex(index) - 1
    val request = AppendEntriesRequest(
      term = term,
      prevLogIndex = prevLogIndex,
      prevLogTerm = log(prevLogIndex).term,
      entries = log.slice(prevLogIndex + 1, log.length),
      leaderCommit = commitIndex.min(log.length),
      uuid = uuid,
      participantIndex = participantIndex
    )
    logger.info(
      s"Sending AppendEntriesRequest to ${nodes(index)}"
        + s" | Term: ${request.term}"
        + s" | PrevLogIndex = ${request.prevLogIndex}"
        + s" | PrevLogTerm = ${request.prevLogTerm}"
        + s" | Entries = ${request.entries}"
        + s" | Leader Commit = ${request.leaderCommit}"
        + s" | UUID = ${request.uuid}"
        + s" | ParticipantIndex: ${request.participantIndex}"
    )
    nodes(index).send(ParticipantInbound().withAppendEntriesRequest(request))
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
