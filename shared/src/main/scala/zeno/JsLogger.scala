package zeno

import scala.collection.mutable.Buffer
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation._

@JSExportAll
object JsLogEntryType extends Enumeration {
  val Debug = Value("DEBUG")
  val Info = Value("INFO")
  val Warn = Value("WARN")
  val Error = Value("ERROR")
  val Fatal = Value("FATAL")
}

@JSExportAll
case class JsLogEntry(typ: JsLogEntryType.Value, text: String)

@JSExportAll
class JsLogger extends Logger {
  private val log = Buffer[JsLogEntry]()
  def logJs(): js.Array[JsLogEntry] = { log.toJSArray }

  override def fatal(message: String): Unit = {
    // TODO(mwhittaker): Crash program.
    log += JsLogEntry(JsLogEntryType.Fatal, s"$message")
  }

  override def error(message: String): Unit = {
    log += JsLogEntry(JsLogEntryType.Error, s"$message")
  }

  override def warn(message: String): Unit = {
    log += JsLogEntry(JsLogEntryType.Warn, s"$message")
  }

  override def info(message: String): Unit = {
    log += JsLogEntry(JsLogEntryType.Info, s"$message")
  }

  override def debug(message: String): Unit = {
    log += JsLogEntry(JsLogEntryType.Debug, s"$message")
  }
}
