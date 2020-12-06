// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.matchmakermultipaxos

@SerialVersionUID(0L)
final case class RepeatedLeaderReconfigurationProto(
    acceptor: _root_.scala.collection.Seq[_root_.scala.Int] = _root_.scala.collection.Seq.empty,
    delayMs: _root_.scala.Int,
    periodMs: _root_.scala.Int
    ) extends scalapb.GeneratedMessage with scalapb.Message[RepeatedLeaderReconfigurationProto] with scalapb.lenses.Updatable[RepeatedLeaderReconfigurationProto] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      acceptor.foreach(acceptor => __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, acceptor))
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, delayMs)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(3, periodMs)
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
      acceptor.foreach { __v =>
        _output__.writeInt32(1, __v)
      };
      _output__.writeInt32(2, delayMs)
      _output__.writeInt32(3, periodMs)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto = {
      val __acceptor = (_root_.scala.collection.immutable.Vector.newBuilder[_root_.scala.Int] ++= this.acceptor)
      var __delayMs = this.delayMs
      var __periodMs = this.periodMs
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __acceptor += _input__.readInt32()
          case 10 => {
            val length = _input__.readRawVarint32()
            val oldLimit = _input__.pushLimit(length)
            while (_input__.getBytesUntilLimit > 0) {
              __acceptor += _input__.readInt32
            }
            _input__.popLimit(oldLimit)
          }
          case 16 =>
            __delayMs = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 24 =>
            __periodMs = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto(
          acceptor = __acceptor.result(),
          delayMs = __delayMs,
          periodMs = __periodMs
      )
    }
    def clearAcceptor = copy(acceptor = _root_.scala.collection.Seq.empty)
    def addAcceptor(__vs: _root_.scala.Int*): RepeatedLeaderReconfigurationProto = addAllAcceptor(__vs)
    def addAllAcceptor(__vs: TraversableOnce[_root_.scala.Int]): RepeatedLeaderReconfigurationProto = copy(acceptor = acceptor ++ __vs)
    def withAcceptor(__v: _root_.scala.collection.Seq[_root_.scala.Int]): RepeatedLeaderReconfigurationProto = copy(acceptor = __v)
    def withDelayMs(__v: _root_.scala.Int): RepeatedLeaderReconfigurationProto = copy(delayMs = __v)
    def withPeriodMs(__v: _root_.scala.Int): RepeatedLeaderReconfigurationProto = copy(periodMs = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => acceptor
        case 2 => delayMs
        case 3 => periodMs
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PRepeated(acceptor.map(_root_.scalapb.descriptors.PInt)(_root_.scala.collection.breakOut))
        case 2 => _root_.scalapb.descriptors.PInt(delayMs)
        case 3 => _root_.scalapb.descriptors.PInt(periodMs)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto
}

object RepeatedLeaderReconfigurationProto extends scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto(
      __fieldsMap.getOrElse(__fields.get(0), Nil).asInstanceOf[_root_.scala.collection.Seq[_root_.scala.Int]],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(2)).asInstanceOf[_root_.scala.Int]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.collection.Seq[_root_.scala.Int]]).getOrElse(_root_.scala.collection.Seq.empty),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).get.as[_root_.scala.Int]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = DriverWorkloadProtoCompanion.javaDescriptor.getMessageTypes.get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = DriverWorkloadProtoCompanion.scalaDescriptor.messages(1)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto(
    delayMs = 0,
    periodMs = 0
  )
  implicit class RepeatedLeaderReconfigurationProtoLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.matchmakermultipaxos.RepeatedLeaderReconfigurationProto](_l) {
    def acceptor: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[_root_.scala.Int]] = field(_.acceptor)((c_, f_) => c_.copy(acceptor = f_))
    def delayMs: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.delayMs)((c_, f_) => c_.copy(delayMs = f_))
    def periodMs: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.periodMs)((c_, f_) => c_.copy(periodMs = f_))
  }
  final val ACCEPTOR_FIELD_NUMBER = 1
  final val DELAY_MS_FIELD_NUMBER = 2
  final val PERIOD_MS_FIELD_NUMBER = 3
}
