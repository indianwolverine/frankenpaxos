// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.compact

@SerialVersionUID(0L)
final case class IntPrefixSetProto(
    watermark: _root_.scala.Int,
    value: _root_.scala.collection.Seq[_root_.scala.Int] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[IntPrefixSetProto] with scalapb.lenses.Updatable[IntPrefixSetProto] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, watermark)
      value.foreach(value => __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, value))
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
      _output__.writeInt32(1, watermark)
      value.foreach { __v =>
        _output__.writeInt32(2, __v)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.compact.IntPrefixSetProto = {
      var __watermark = this.watermark
      val __value = (_root_.scala.collection.immutable.Vector.newBuilder[_root_.scala.Int] ++= this.value)
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __watermark = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __value += _input__.readInt32()
          case 18 => {
            val length = _input__.readRawVarint32()
            val oldLimit = _input__.pushLimit(length)
            while (_input__.getBytesUntilLimit > 0) {
              __value += _input__.readInt32
            }
            _input__.popLimit(oldLimit)
          }
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.compact.IntPrefixSetProto(
          watermark = __watermark,
          value = __value.result()
      )
    }
    def withWatermark(__v: _root_.scala.Int): IntPrefixSetProto = copy(watermark = __v)
    def clearValue = copy(value = _root_.scala.collection.Seq.empty)
    def addValue(__vs: _root_.scala.Int*): IntPrefixSetProto = addAllValue(__vs)
    def addAllValue(__vs: TraversableOnce[_root_.scala.Int]): IntPrefixSetProto = copy(value = value ++ __vs)
    def withValue(__v: _root_.scala.collection.Seq[_root_.scala.Int]): IntPrefixSetProto = copy(value = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => watermark
        case 2 => value
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(watermark)
        case 2 => _root_.scalapb.descriptors.PRepeated(value.map(_root_.scalapb.descriptors.PInt)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.compact.IntPrefixSetProto
}

object IntPrefixSetProto extends scalapb.GeneratedMessageCompanion[frankenpaxos.compact.IntPrefixSetProto] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.compact.IntPrefixSetProto] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.compact.IntPrefixSetProto = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.compact.IntPrefixSetProto(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap.getOrElse(__fields.get(1), Nil).asInstanceOf[_root_.scala.collection.Seq[_root_.scala.Int]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.compact.IntPrefixSetProto] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.compact.IntPrefixSetProto(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.collection.Seq[_root_.scala.Int]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = IntPrefixSetProtoCompanion.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = IntPrefixSetProtoCompanion.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.compact.IntPrefixSetProto(
    watermark = 0
  )
  implicit class IntPrefixSetProtoLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.compact.IntPrefixSetProto]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.compact.IntPrefixSetProto](_l) {
    def watermark: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.watermark)((c_, f_) => c_.copy(watermark = f_))
    def value: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[_root_.scala.Int]] = field(_.value)((c_, f_) => c_.copy(value = f_))
  }
  final val WATERMARK_FIELD_NUMBER = 1
  final val VALUE_FIELD_NUMBER = 2
}
