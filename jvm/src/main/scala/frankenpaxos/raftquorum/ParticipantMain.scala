package frankenpaxos.raftquorum

import frankenpaxos.Actor
import frankenpaxos.Flags.durationRead
import frankenpaxos.LogLevel
import frankenpaxos.NettyTcpAddress
import frankenpaxos.NettyTcpTransport
import frankenpaxos.PrintLogger
import frankenpaxos.PrometheusUtil
import frankenpaxos.Transport
import frankenpaxos.statemachine
import frankenpaxos.statemachine.{
  StateMachine,
  KeyValueStore,
  AppendLog,
}
import frankenpaxos.raft.ElectionOptions
import frankenpaxos.quorums.SimpleMajority
import frankenpaxos.quorums.Grid
import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.hotspot.DefaultExports
import java.io.File
import java.net.InetAddress
import java.net.InetSocketAddress
import scala.concurrent.duration
import scala.collection.mutable.ArrayBuffer

object ParticipantMain extends App {
  case class Flags(
      // Basic flags.
      index: Int = -1,
      configFile: File = new File("."),
      logLevel: frankenpaxos.LogLevel = frankenpaxos.LogDebug,
      stateMachine: StateMachine = new KeyValueStore(),
      // Monitoring.
      prometheusHost: String = "0.0.0.0",
      prometheusPort: Int = 8009,
      // Options.
      options: ElectionOptions = ElectionOptions.default,
      quorumSystem: String = "MAJORITY",
      rowSize: Int = 0,
  )

  implicit class OptionsWrapper[A](o: scopt.OptionDef[A, Flags]) {
    def optionAction(
        f: (A, ElectionOptions) => ElectionOptions
    ): scopt.OptionDef[A, Flags] =
      o.action((x, flags) => flags.copy(options = f(x, flags.options)))
  }

  val parser = new scopt.OptionParser[Flags]("") {
    help("help")

    // Basic flags.
    opt[Int]("index").required().action((x, f) => f.copy(index = x))
    opt[File]("config").required().action((x, f) => f.copy(configFile = x))
    opt[LogLevel]("log_level").required().action((x, f) => f.copy(logLevel = x))
    opt[StateMachine]("state_machine")
      .required()
      .action((x, f) => f.copy(stateMachine = x))
    opt[String]("quorum_system")
      .action((x, f) => f.copy(quorumSystem = x))
    opt[Int]("row_size")
      .action((x, f) => f.copy(rowSize = x))

    // Monitoring.
    opt[String]("prometheus_host")
      .action((x, f) => f.copy(prometheusHost = x))
    opt[Int]("prometheus_port")
      .action((x, f) => f.copy(prometheusPort = x))
      .text(s"-1 to disable")

    // Options.
    opt[java.time.Duration]("options.pingPeriod")
      .optionAction((x, o) => o.copy(pingPeriod = x))
    opt[java.time.Duration]("options.noPingTimeoutMin")
      .optionAction((x, o) => o.copy(noPingTimeoutMin = x))  
    opt[java.time.Duration]("options.noPingTimeoutMax")
      .optionAction((x, o) => o.copy(noPingTimeoutMax = x))
    opt[java.time.Duration]("options.notEnoughVotesTimeoutMin")
      .optionAction((x, o) => o.copy(notEnoughVotesTimeoutMin = x))
    opt[java.time.Duration]("options.notEnoughVotesTimeoutMax")
      .optionAction((x, o) => o.copy(notEnoughVotesTimeoutMax = x))
  }

  // Parse flags.
  val flags: Flags = parser.parse(args, Flags()) match {
    case Some(flags) =>
      flags
    case None =>
      throw new IllegalArgumentException("Could not parse flags.")
  }

  // Construct participant.
  val logger = new PrintLogger(flags.logLevel)
  val transport = new NettyTcpTransport(logger)
  val config = ConfigUtil.fromFile(flags.configFile.getAbsolutePath())
  val quorumSystem = flags.quorumSystem match {
    case "MAJORITY" => new SimpleMajority(
      (0 until config.participantAddresses.size).toSet,
    )
    case "GRID" => {
      val numRows: Int = config.participantAddresses.size / flags.rowSize
      var grid: ArrayBuffer[ArrayBuffer[Int]] = new ArrayBuffer[ArrayBuffer[Int]]()
      for (i <- 0 until numRows) {
        grid += new ArrayBuffer[Int]()
      }
      var j = 0
      for (i <- 0 until config.participantAddresses.size) {
        grid(j) += i
        j += 1
        if (j == numRows) {
          j = 0
        }
      }
      new Grid(grid)
    }
  }
  val participant = new QuorumParticipant[NettyTcpTransport](
    address = config.participantAddresses(flags.index),
    transport = transport,
    logger = logger,
    config = config,
    stateMachine = flags.stateMachine,
    quorumSystem = quorumSystem,
    options = flags.options,
    leader = Some(config.participantAddresses(0)),
    participantIndex = flags.index
  )

  // Start Prometheus.
  PrometheusUtil.server(flags.prometheusHost, flags.prometheusPort)
}
