package frankenpaxos.spaxosdecouple

import java.io.File

import frankenpaxos.statemachine.StateMachine
import frankenpaxos.{NettyTcpTransport, PrintLogger, statemachine}
import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.hotspot.DefaultExports

import scala.concurrent.duration

object ExecutorMain extends App {
  case class Flags(
      // Basic flags.
      index: Int = -1,
      paxosConfigFile: File = new File("."),
      stateMachine: StateMachine = new statemachine.Noop(),
      // Monitoring.
      prometheusHost: String = "0.0.0.0",
      prometheusPort: Int = 8009,
      // Options.
      options: ExecutorOptions = ExecutorOptions.default
  )

  val parser = new scopt.OptionParser[Flags]("") {
    opt[StateMachine]("state_machine")
      .required()
      .action((x, f) => f.copy(stateMachine = x))
    // Basic flags.
    opt[Int]('i', "index")
      .required()
      .action((x, f) => f.copy(index = x))

    opt[File]('c', "config")
      .required()
      .action((x, f) => f.copy(paxosConfigFile = x))

    // Monitoring.
    opt[String]("prometheus_host")
      .action((x, f) => f.copy(prometheusHost = x))

    opt[Int]("prometheus_port")
      .action((x, f) => f.copy(prometheusPort = x))
      .text(s"-1 to disable")

    // Options.
    opt[duration.Duration]("options.waitPeriod")
      .action((x, f) => {
        f.copy(
          options = f.options
            .copy(waitPeriod = java.time.Duration.ofNanos(x.toNanos))
        )
      })

    opt[duration.Duration]("options.waitStagger")
      .action((x, f) => {
        f.copy(
          options = f.options
            .copy(waitStagger = java.time.Duration.ofNanos(x.toNanos))
        )
      })

  }

  val flags: Flags = parser.parse(args, Flags()) match {
    case Some(flags) => flags
    case None        => ???
  }

  val logger = new PrintLogger()
  val transport = new NettyTcpTransport(logger)
  val config = ConfigUtil.fromFile(flags.paxosConfigFile.getAbsolutePath())
  val address = config.executorAddresses(flags.index)
  val executor = new Executor[NettyTcpTransport](address,
                                                 transport,
                                                 logger,
                                                 config,
                                                 flags.stateMachine,
                                                 flags.options)

  if (flags.prometheusPort != -1) {
    DefaultExports.initialize()
    val prometheusServer =
      new HTTPServer(flags.prometheusHost, flags.prometheusPort)
    logger.info(
      s"Prometheus server running on ${flags.prometheusHost}:" +
        s"${flags.prometheusPort}"
    )
  } else {
    logger.info(
      s"Prometheus server not running because a port of -1 was given."
    )
  }
}