// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.horizontal

/** @param slot
  *   Replicas execute logs in prefix order. Thus, if the log permanently has a
  *   hole in it, the algorithm remains forever blocked. To solve this, if a
  *   replica notices a hole in its log for a certain amount of time, it sends a
  *   Recover message to the leader to get the hole plugged.
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Recover(
    slot: _root_.scala.Int
    ) extends scalapb.GeneratedMessage with scalapb.Message[Recover] with scalapb.lenses.Updatable[Recover] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, slot)
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
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.horizontal.Recover = {
      var __slot = this.slot
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __slot = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.horizontal.Recover(
          slot = __slot
      )
    }
    def withSlot(__v: _root_.scala.Int): Recover = copy(slot = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => slot
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(slot)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.horizontal.Recover
}

object Recover extends scalapb.GeneratedMessageCompanion[frankenpaxos.horizontal.Recover] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.horizontal.Recover] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.horizontal.Recover = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.horizontal.Recover(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.horizontal.Recover] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.horizontal.Recover(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = HorizontalProto.javaDescriptor.getMessageTypes.get(18)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = HorizontalProto.scalaDescriptor.messages(18)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.horizontal.Recover(
    slot = 0
  )
  implicit class RecoverLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.horizontal.Recover]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.horizontal.Recover](_l) {
    def slot: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.slot)((c_, f_) => c_.copy(slot = f_))
  }
  final val SLOT_FIELD_NUMBER = 1
}
