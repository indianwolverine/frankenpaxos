// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.multipaxos

/** Helper messages. ////////////////////////////////////////////////////////////
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Noop(
    ) extends scalapb.GeneratedMessage with scalapb.Message[Noop] with scalapb.lenses.Updatable[Noop] {
    final override def serializedSize: _root_.scala.Int = 0
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.multipaxos.Noop = {
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.multipaxos.Noop(
      )
    }
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = throw new MatchError(__fieldNumber)
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = throw new MatchError(__field)
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.multipaxos.Noop
}

object Noop extends scalapb.GeneratedMessageCompanion[frankenpaxos.multipaxos.Noop] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.multipaxos.Noop] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.multipaxos.Noop = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    frankenpaxos.multipaxos.Noop(
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.multipaxos.Noop] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.multipaxos.Noop(
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MultiPaxosProto.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MultiPaxosProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.multipaxos.Noop(
  )
  implicit class NoopLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.multipaxos.Noop]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.multipaxos.Noop](_l) {
  }
}
