package frankenpaxos.raftquorum

import frankenpaxos.Actor
import frankenpaxos.BenchmarkUtil
import frankenpaxos.FileLogger
import frankenpaxos.Flags.durationRead
import frankenpaxos.LogLevel
import frankenpaxos.NettyTcpAddress
import frankenpaxos.NettyTcpTransport
import frankenpaxos.PrintLogger
import frankenpaxos.PrometheusUtil
import frankenpaxos.Transport
import frankenpaxos.quorums.SimpleMajority
import frankenpaxos.quorums.Grid
import frankenpaxos.monitoring.PrometheusCollectors
import java.io.File
import java.net.InetAddress
import java.net.InetSocketAddress
import org.scalacheck.Gen
import org.scalacheck.rng.Seed
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer

object ClientMain extends App {
  case class Flags(
      // Basic flags.
      host: String = "localhost",
      port: Int = 9000,
      configFile: File = new File("."),
      logLevel: frankenpaxos.LogLevel = frankenpaxos.LogDebug,
      // Monitoring.
      prometheusHost: String = "0.0.0.0",
      prometheusPort: Int = 8009,
      // Benchmark flags.
      measurementGroupSize: Int = 1,
      warmupDuration: java.time.Duration = java.time.Duration.ofSeconds(5),
      warmupTimeout: Duration = 10 seconds,
      warmupSleep: java.time.Duration = java.time.Duration.ofSeconds(0),
      duration: java.time.Duration = java.time.Duration.ofSeconds(5),
      timeout: Duration = 10 seconds,
      outputFilePrefix: String = "",
      workload: ReadWriteWorkload = new UniformReadWriteWorkload(1, 1, 1, 0),
      quorumSystem: String = "MAJORITY",
      rowSize: Int = 0,
  )

  val parser = new scopt.OptionParser[Flags]("") {
    // Basic flags.
    opt[String]("host").required().action((x, f) => f.copy(host = x))
    opt[Int]("port").required().action((x, f) => f.copy(port = x))
    opt[File]("config").required().action((x, f) => f.copy(configFile = x))
    opt[LogLevel]("log_level").required().action((x, f) => f.copy(logLevel = x))

    // Monitoring.
    opt[String]("prometheus_host")
      .action((x, f) => f.copy(prometheusHost = x))
    opt[Int]("prometheus_port")
      .action((x, f) => f.copy(prometheusPort = x))
      .text(s"Prometheus port; -1 to disable")

    // Benchmark flags.
    opt[Int]("measurement_group_size")
      .action((x, f) => f.copy(measurementGroupSize = x))
    opt[java.time.Duration]("warmup_duration")
      .action((x, f) => f.copy(warmupDuration = x))
    opt[Duration]("warmup_timeout")
      .action((x, f) => f.copy(warmupTimeout = x))
    opt[java.time.Duration]("warmup_sleep")
      .action((x, f) => f.copy(warmupSleep = x))
    opt[java.time.Duration]("duration")
      .action((x, f) => f.copy(duration = x))
    opt[Duration]("timeout")
      .action((x, f) => f.copy(timeout = x))
    opt[String]("output_file_prefix")
      .action((x, f) => f.copy(outputFilePrefix = x))
    opt[ReadWriteWorkload]("workload")
      .action((x, f) => f.copy(workload = x))
    opt[String]("quorum_system")
      .action((x, f) => f.copy(quorumSystem = x))
    opt[Int]("row_size")
      .action((x, f) => f.copy(rowSize = x))
  }

  val flags: Flags = parser.parse(args, Flags()) match {
    case Some(flags) =>
      flags
    case None =>
      throw new IllegalArgumentException("Could not parse flags.")
  }

  // Start prometheus.
  val prometheusServer =
    PrometheusUtil.server(flags.prometheusHost, flags.prometheusPort)

  // Construct client.
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
  val client = new QuorumClient[NettyTcpTransport](
    srcAddress = NettyTcpAddress(new InetSocketAddress(flags.host, flags.port)),
    transport = transport,
    logger = logger,
    config = config,
    quorumSystem = quorumSystem,
  )

  val recorder = new BenchmarkUtil.LabeledRecorder(
    s"${flags.outputFilePrefix}_data.csv",
    groupSize = flags.measurementGroupSize
  )
  def run(workload: ReadWriteWorkload): Future[Unit] = {
    implicit val context = transport.executionContext
    val (f, error, label) = workload.get() match {
      case Write(command) =>
        (() => client.write(command), "Write failed.", "write")
      case Read(command) =>
        (() => client.read(command), "Read failed.", "read")
    }

    BenchmarkUtil
      .timed(f)
      .transformWith({
        case scala.util.Failure(_) =>
          logger.debug(error)
          Future.successful(())

        case scala.util.Success((_, timing)) =>
          recorder.record(
            start = timing.startTime,
            stop = timing.stopTime,
            latencyNanos = timing.durationNanos,
            label = label
          )
          Future.successful(())
      })
  }

  implicit val context = transport.executionContext

  // Sleep to let protocol settle.
  Thread.sleep(flags.warmupSleep.toMillis())

  // Run the benchmark.
  try {
    logger.info("Clients started.")
    concurrent.Await.result(BenchmarkUtil.runFor(() => run(flags.workload), flags.duration), flags.timeout)
    logger.info("Clients finished successfully.")
  } catch {
    case e: java.util.concurrent.TimeoutException =>
      logger.warn("Client futures timed out!")
      logger.warn(e.toString())
  }
  recorder.flush()

  // Shut everything down.
  logger.info("Shutting down transport.")
  transport.shutdown()
  logger.info("Transport shut down.")

  prometheusServer.foreach(server => {
    logger.info("Stopping prometheus.")
    server.stop()
    logger.info("Prometheus stopped.")
  })
}
