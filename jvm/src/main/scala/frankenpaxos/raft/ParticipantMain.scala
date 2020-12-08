package frankenpaxos.raft

import frankenpaxos.Actor
import frankenpaxos.Flags.durationRead
import frankenpaxos.LogLevel
import frankenpaxos.NettyTcpAddress
import frankenpaxos.NettyTcpTransport
import frankenpaxos.PrintLogger
import frankenpaxos.PrometheusUtil
import frankenpaxos.statemachine
import frankenpaxos.statemachine.StateMachine
import frankenpaxos.statemachine.AppendLog
import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.hotspot.DefaultExports
import java.io.File
import java.net.InetAddress
import java.net.InetSocketAddress
import scala.concurrent.duration

object ParticipantMain extends App {
  case class Flags(
      // Basic flags.
      groupIndex: Int = -1,
      index: Int = -1,
      configFile: File = new File("."),
      logLevel: frankenpaxos.LogLevel = frankenpaxos.LogDebug,
      stateMachine: StateMachine = new statemachine.Noop(),
      // Monitoring.
      prometheusHost: String = "0.0.0.0",
      prometheusPort: Int = 8009,
      // Options.
      options: ElectionOptions = ElectionOptions.default
  )

  val parser = new scopt.OptionParser[Flags]("") {
    help("help")

    // Basic flags.
    opt[Int]("group_index").required().action((x, f) => f.copy(groupIndex = x))
    opt[Int]("index").required().action((x, f) => f.copy(index = x))
    opt[File]("config").required().action((x, f) => f.copy(configFile = x))
    opt[LogLevel]("log_level").required().action((x, f) => f.copy(logLevel = x))
    opt[StateMachine]("state_machine")
      .required()
      .action((x, f) => f.copy(stateMachine = x))

    // Monitoring.
    opt[String]("prometheus_host")
      .action((x, f) => f.copy(prometheusHost = x))
    opt[Int]("prometheus_port")
      .action((x, f) => f.copy(prometheusPort = x))
      .text(s"-1 to disable")
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
  val config = ConfigUtil.fromFile(flags.configFile.getAbsolutePath())
  val participant = new Participant[NettyTcpTransport](
    address = config.acceptorAddresses(flags.groupIndex)(flags.index),
    transport = new NettyTcpTransport(logger),
    logger = logger,
    config = config,
    stateMachine = flags.stateMachine,
    options = flags.options
  )

  // Start Prometheus.
  PrometheusUtil.server(flags.prometheusHost, flags.prometheusPort)
}
