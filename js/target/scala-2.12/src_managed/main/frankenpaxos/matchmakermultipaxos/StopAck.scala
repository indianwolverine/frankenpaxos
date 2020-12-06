// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.matchmakermultipaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class StopAck(
    epoch: _root_.scala.Int,
    matchmakerIndex: _root_.scala.Int,
    gcWatermark: _root_.scala.Int,
    configuration: _root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[StopAck] with scalapb.lenses.Updatable[StopAck] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, epoch)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, matchmakerIndex)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(3, gcWatermark)
      configuration.foreach(configuration => __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(configuration.serializedSize) + configuration.serializedSize)
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
      _output__.writeInt32(1, epoch)
      _output__.writeInt32(2, matchmakerIndex)
      _output__.writeInt32(3, gcWatermark)
      configuration.foreach { __v =>
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.matchmakermultipaxos.StopAck = {
      var __epoch = this.epoch
      var __matchmakerIndex = this.matchmakerIndex
      var __gcWatermark = this.gcWatermark
      val __configuration = (_root_.scala.collection.immutable.Vector.newBuilder[frankenpaxos.matchmakermultipaxos.Configuration] ++= this.configuration)
      var __requiredFields0: _root_.scala.Long = 0x7L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __epoch = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __matchmakerIndex = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case 24 =>
            __gcWatermark = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffbL
          case 34 =>
            __configuration += _root_.scalapb.LiteParser.readMessage(_input__, frankenpaxos.matchmakermultipaxos.Configuration.defaultInstance)
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.matchmakermultipaxos.StopAck(
          epoch = __epoch,
          matchmakerIndex = __matchmakerIndex,
          gcWatermark = __gcWatermark,
          configuration = __configuration.result()
      )
    }
    def withEpoch(__v: _root_.scala.Int): StopAck = copy(epoch = __v)
    def withMatchmakerIndex(__v: _root_.scala.Int): StopAck = copy(matchmakerIndex = __v)
    def withGcWatermark(__v: _root_.scala.Int): StopAck = copy(gcWatermark = __v)
    def clearConfiguration = copy(configuration = _root_.scala.collection.Seq.empty)
    def addConfiguration(__vs: frankenpaxos.matchmakermultipaxos.Configuration*): StopAck = addAllConfiguration(__vs)
    def addAllConfiguration(__vs: TraversableOnce[frankenpaxos.matchmakermultipaxos.Configuration]): StopAck = copy(configuration = configuration ++ __vs)
    def withConfiguration(__v: _root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]): StopAck = copy(configuration = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => epoch
        case 2 => matchmakerIndex
        case 3 => gcWatermark
        case 4 => configuration
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(epoch)
        case 2 => _root_.scalapb.descriptors.PInt(matchmakerIndex)
        case 3 => _root_.scalapb.descriptors.PInt(gcWatermark)
        case 4 => _root_.scalapb.descriptors.PRepeated(configuration.map(_.toPMessage)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.matchmakermultipaxos.StopAck
}

object StopAck extends scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakermultipaxos.StopAck] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakermultipaxos.StopAck] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.matchmakermultipaxos.StopAck = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.matchmakermultipaxos.StopAck(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(2)).asInstanceOf[_root_.scala.Int],
      __fieldsMap.getOrElse(__fields.get(3), Nil).asInstanceOf[_root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.matchmakermultipaxos.StopAck] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.matchmakermultipaxos.StopAck(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MatchmakerMultiPaxosProto.javaDescriptor.getMessageTypes.get(33)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MatchmakerMultiPaxosProto.scalaDescriptor.messages(33)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 4 => __out = frankenpaxos.matchmakermultipaxos.Configuration
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.matchmakermultipaxos.StopAck(
    epoch = 0,
    matchmakerIndex = 0,
    gcWatermark = 0
  )
  implicit class StopAckLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakermultipaxos.StopAck]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.matchmakermultipaxos.StopAck](_l) {
    def epoch: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.epoch)((c_, f_) => c_.copy(epoch = f_))
    def matchmakerIndex: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.matchmakerIndex)((c_, f_) => c_.copy(matchmakerIndex = f_))
    def gcWatermark: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.gcWatermark)((c_, f_) => c_.copy(gcWatermark = f_))
    def configuration: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]] = field(_.configuration)((c_, f_) => c_.copy(configuration = f_))
  }
  final val EPOCH_FIELD_NUMBER = 1
  final val MATCHMAKER_INDEX_FIELD_NUMBER = 2
  final val GCWATERMARK_FIELD_NUMBER = 3
  final val CONFIGURATION_FIELD_NUMBER = 4
}
