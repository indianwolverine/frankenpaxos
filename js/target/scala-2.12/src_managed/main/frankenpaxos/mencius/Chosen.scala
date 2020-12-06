// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.mencius

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Chosen(
    slot: _root_.scala.Int,
    commandBatchOrNoop: frankenpaxos.mencius.CommandBatchOrNoop
    ) extends scalapb.GeneratedMessage with scalapb.Message[Chosen] with scalapb.lenses.Updatable[Chosen] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, slot)
      __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(commandBatchOrNoop.serializedSize) + commandBatchOrNoop.serializedSize
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
      _output__.writeInt32(1, slot)
      _output__.writeTag(2, 2)
      _output__.writeUInt32NoTag(commandBatchOrNoop.serializedSize)
      commandBatchOrNoop.writeTo(_output__)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.mencius.Chosen = {
      var __slot = this.slot
      var __commandBatchOrNoop = this.commandBatchOrNoop
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __slot = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 18 =>
            __commandBatchOrNoop = _root_.scalapb.LiteParser.readMessage(_input__, __commandBatchOrNoop)
            __requiredFields0 &= 0xfffffffffffffffdL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.mencius.Chosen(
          slot = __slot,
          commandBatchOrNoop = __commandBatchOrNoop
      )
    }
    def withSlot(__v: _root_.scala.Int): Chosen = copy(slot = __v)
    def withCommandBatchOrNoop(__v: frankenpaxos.mencius.CommandBatchOrNoop): Chosen = copy(commandBatchOrNoop = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => slot
        case 2 => commandBatchOrNoop
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(slot)
        case 2 => commandBatchOrNoop.toPMessage
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.mencius.Chosen
}

object Chosen extends scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.Chosen] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.Chosen] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.mencius.Chosen = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.mencius.Chosen(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[frankenpaxos.mencius.CommandBatchOrNoop]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.mencius.Chosen] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.mencius.Chosen(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[frankenpaxos.mencius.CommandBatchOrNoop]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MenciusProto.javaDescriptor.getMessageTypes.get(15)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MenciusProto.scalaDescriptor.messages(15)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = frankenpaxos.mencius.CommandBatchOrNoop
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.mencius.Chosen(
    slot = 0,
    commandBatchOrNoop = frankenpaxos.mencius.CommandBatchOrNoop.defaultInstance
  )
  implicit class ChosenLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.Chosen]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.mencius.Chosen](_l) {
    def slot: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.slot)((c_, f_) => c_.copy(slot = f_))
    def commandBatchOrNoop: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.CommandBatchOrNoop] = field(_.commandBatchOrNoop)((c_, f_) => c_.copy(commandBatchOrNoop = f_))
  }
  final val SLOT_FIELD_NUMBER = 1
  final val COMMAND_BATCH_OR_NOOP_FIELD_NUMBER = 2
}
