// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.mencius

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Phase2b(
    acceptorIndex: _root_.scala.Int,
    slot: _root_.scala.Int,
    round: _root_.scala.Int
    ) extends scalapb.GeneratedMessage with scalapb.Message[Phase2b] with scalapb.lenses.Updatable[Phase2b] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, acceptorIndex)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, slot)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(3, round)
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
      _output__.writeInt32(1, acceptorIndex)
      _output__.writeInt32(2, slot)
      _output__.writeInt32(3, round)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.mencius.Phase2b = {
      var __acceptorIndex = this.acceptorIndex
      var __slot = this.slot
      var __round = this.round
      var __requiredFields0: _root_.scala.Long = 0x7L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __acceptorIndex = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __slot = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case 24 =>
            __round = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffbL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.mencius.Phase2b(
          acceptorIndex = __acceptorIndex,
          slot = __slot,
          round = __round
      )
    }
    def withAcceptorIndex(__v: _root_.scala.Int): Phase2b = copy(acceptorIndex = __v)
    def withSlot(__v: _root_.scala.Int): Phase2b = copy(slot = __v)
    def withRound(__v: _root_.scala.Int): Phase2b = copy(round = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => acceptorIndex
        case 2 => slot
        case 3 => round
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(acceptorIndex)
        case 2 => _root_.scalapb.descriptors.PInt(slot)
        case 3 => _root_.scalapb.descriptors.PInt(round)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.mencius.Phase2b
}

object Phase2b extends scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.Phase2b] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.Phase2b] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.mencius.Phase2b = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.mencius.Phase2b(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(2)).asInstanceOf[_root_.scala.Int]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.mencius.Phase2b] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.mencius.Phase2b(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).get.as[_root_.scala.Int]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MenciusProto.javaDescriptor.getMessageTypes.get(13)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MenciusProto.scalaDescriptor.messages(13)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.mencius.Phase2b(
    acceptorIndex = 0,
    slot = 0,
    round = 0
  )
  implicit class Phase2bLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.Phase2b]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.mencius.Phase2b](_l) {
    def acceptorIndex: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.acceptorIndex)((c_, f_) => c_.copy(acceptorIndex = f_))
    def slot: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.slot)((c_, f_) => c_.copy(slot = f_))
    def round: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.round)((c_, f_) => c_.copy(round = f_))
  }
  final val ACCEPTOR_INDEX_FIELD_NUMBER = 1
  final val SLOT_FIELD_NUMBER = 2
  final val ROUND_FIELD_NUMBER = 3
}
