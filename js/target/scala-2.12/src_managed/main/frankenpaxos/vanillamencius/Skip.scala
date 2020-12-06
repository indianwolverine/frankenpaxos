// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.vanillamencius

/** @param serverIndex
  *   Note that we don't have a round because a Skip is always in round 0.
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Skip(
    serverIndex: _root_.scala.Int,
    startSlotInclusive: _root_.scala.Int,
    stopSlotExclusive: _root_.scala.Int
    ) extends scalapb.GeneratedMessage with scalapb.Message[Skip] with scalapb.lenses.Updatable[Skip] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, serverIndex)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, startSlotInclusive)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(3, stopSlotExclusive)
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
      _output__.writeInt32(1, serverIndex)
      _output__.writeInt32(2, startSlotInclusive)
      _output__.writeInt32(3, stopSlotExclusive)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.vanillamencius.Skip = {
      var __serverIndex = this.serverIndex
      var __startSlotInclusive = this.startSlotInclusive
      var __stopSlotExclusive = this.stopSlotExclusive
      var __requiredFields0: _root_.scala.Long = 0x7L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __serverIndex = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __startSlotInclusive = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case 24 =>
            __stopSlotExclusive = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffbL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.vanillamencius.Skip(
          serverIndex = __serverIndex,
          startSlotInclusive = __startSlotInclusive,
          stopSlotExclusive = __stopSlotExclusive
      )
    }
    def withServerIndex(__v: _root_.scala.Int): Skip = copy(serverIndex = __v)
    def withStartSlotInclusive(__v: _root_.scala.Int): Skip = copy(startSlotInclusive = __v)
    def withStopSlotExclusive(__v: _root_.scala.Int): Skip = copy(stopSlotExclusive = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => serverIndex
        case 2 => startSlotInclusive
        case 3 => stopSlotExclusive
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(serverIndex)
        case 2 => _root_.scalapb.descriptors.PInt(startSlotInclusive)
        case 3 => _root_.scalapb.descriptors.PInt(stopSlotExclusive)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.vanillamencius.Skip
}

object Skip extends scalapb.GeneratedMessageCompanion[frankenpaxos.vanillamencius.Skip] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.vanillamencius.Skip] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.vanillamencius.Skip = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.vanillamencius.Skip(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(2)).asInstanceOf[_root_.scala.Int]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.vanillamencius.Skip] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.vanillamencius.Skip(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).get.as[_root_.scala.Int]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = VanillaMenciusProto.javaDescriptor.getMessageTypes.get(11)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = VanillaMenciusProto.scalaDescriptor.messages(11)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.vanillamencius.Skip(
    serverIndex = 0,
    startSlotInclusive = 0,
    stopSlotExclusive = 0
  )
  implicit class SkipLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.vanillamencius.Skip]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.vanillamencius.Skip](_l) {
    def serverIndex: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.serverIndex)((c_, f_) => c_.copy(serverIndex = f_))
    def startSlotInclusive: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.startSlotInclusive)((c_, f_) => c_.copy(startSlotInclusive = f_))
    def stopSlotExclusive: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.stopSlotExclusive)((c_, f_) => c_.copy(stopSlotExclusive = f_))
  }
  final val SERVER_INDEX_FIELD_NUMBER = 1
  final val START_SLOT_INCLUSIVE_FIELD_NUMBER = 2
  final val STOP_SLOT_EXCLUSIVE_FIELD_NUMBER = 3
}
