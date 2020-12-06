// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.multipaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Command(
    commandId: frankenpaxos.multipaxos.CommandId,
    command: _root_.com.google.protobuf.ByteString
    ) extends scalapb.GeneratedMessage with scalapb.Message[Command] with scalapb.lenses.Updatable[Command] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(commandId.serializedSize) + commandId.serializedSize
      __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(2, command)
      __size
    }
    final override def serializedSize: _root_.scala.Int = {
      var read = __serializedSizeCachedValue
      if (read == 0) {
        read = __computeSerializedValue()
        __serializedSizeCachedValue = read
      }
      read
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
      _output__.writeTag(1, 2)
      _output__.writeUInt32NoTag(commandId.serializedSize)
      commandId.writeTo(_output__)
      _output__.writeBytes(2, command)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.multipaxos.Command = {
      var __commandId = this.commandId
      var __command = this.command
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __commandId = _root_.scalapb.LiteParser.readMessage(_input__, __commandId)
            __requiredFields0 &= 0xfffffffffffffffeL
          case 18 =>
            __command = _input__.readBytes()
            __requiredFields0 &= 0xfffffffffffffffdL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.multipaxos.Command(
          commandId = __commandId,
          command = __command
      )
    }
    def withCommandId(__v: frankenpaxos.multipaxos.CommandId): Command = copy(commandId = __v)
    def withCommand(__v: _root_.com.google.protobuf.ByteString): Command = copy(command = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => commandId
        case 2 => command
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => commandId.toPMessage
        case 2 => _root_.scalapb.descriptors.PByteString(command)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.multipaxos.Command
}

object Command extends scalapb.GeneratedMessageCompanion[frankenpaxos.multipaxos.Command] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.multipaxos.Command] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.multipaxos.Command = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.multipaxos.Command(
      __fieldsMap(__fields.get(0)).asInstanceOf[frankenpaxos.multipaxos.CommandId],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.com.google.protobuf.ByteString]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.multipaxos.Command] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.multipaxos.Command(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[frankenpaxos.multipaxos.CommandId],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.com.google.protobuf.ByteString]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MultiPaxosProto.javaDescriptor.getMessageTypes.get(2)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MultiPaxosProto.scalaDescriptor.messages(2)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.multipaxos.CommandId
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.multipaxos.Command(
    commandId = frankenpaxos.multipaxos.CommandId.defaultInstance,
    command = _root_.com.google.protobuf.ByteString.EMPTY
  )
  implicit class CommandLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.multipaxos.Command]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.multipaxos.Command](_l) {
    def commandId: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.multipaxos.CommandId] = field(_.commandId)((c_, f_) => c_.copy(commandId = f_))
    def command: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.command)((c_, f_) => c_.copy(command = f_))
  }
  final val COMMAND_ID_FIELD_NUMBER = 1
  final val COMMAND_FIELD_NUMBER = 2
}
