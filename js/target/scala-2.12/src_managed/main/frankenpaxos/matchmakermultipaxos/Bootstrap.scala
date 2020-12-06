// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.matchmakermultipaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Bootstrap(
    epoch: _root_.scala.Int,
    reconfigurerIndex: _root_.scala.Int,
    gcWatermark: _root_.scala.Int,
    configuration: _root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[Bootstrap] with scalapb.lenses.Updatable[Bootstrap] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, epoch)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, reconfigurerIndex)
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
      _output__.writeInt32(2, reconfigurerIndex)
      _output__.writeInt32(3, gcWatermark)
      configuration.foreach { __v =>
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.matchmakermultipaxos.Bootstrap = {
      var __epoch = this.epoch
      var __reconfigurerIndex = this.reconfigurerIndex
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
            __reconfigurerIndex = _input__.readInt32()
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
      frankenpaxos.matchmakermultipaxos.Bootstrap(
          epoch = __epoch,
          reconfigurerIndex = __reconfigurerIndex,
          gcWatermark = __gcWatermark,
          configuration = __configuration.result()
      )
    }
    def withEpoch(__v: _root_.scala.Int): Bootstrap = copy(epoch = __v)
    def withReconfigurerIndex(__v: _root_.scala.Int): Bootstrap = copy(reconfigurerIndex = __v)
    def withGcWatermark(__v: _root_.scala.Int): Bootstrap = copy(gcWatermark = __v)
    def clearConfiguration = copy(configuration = _root_.scala.collection.Seq.empty)
    def addConfiguration(__vs: frankenpaxos.matchmakermultipaxos.Configuration*): Bootstrap = addAllConfiguration(__vs)
    def addAllConfiguration(__vs: TraversableOnce[frankenpaxos.matchmakermultipaxos.Configuration]): Bootstrap = copy(configuration = configuration ++ __vs)
    def withConfiguration(__v: _root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]): Bootstrap = copy(configuration = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => epoch
        case 2 => reconfigurerIndex
        case 3 => gcWatermark
        case 4 => configuration
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(epoch)
        case 2 => _root_.scalapb.descriptors.PInt(reconfigurerIndex)
        case 3 => _root_.scalapb.descriptors.PInt(gcWatermark)
        case 4 => _root_.scalapb.descriptors.PRepeated(configuration.map(_.toPMessage)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.matchmakermultipaxos.Bootstrap
}

object Bootstrap extends scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakermultipaxos.Bootstrap] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakermultipaxos.Bootstrap] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.matchmakermultipaxos.Bootstrap = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.matchmakermultipaxos.Bootstrap(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(2)).asInstanceOf[_root_.scala.Int],
      __fieldsMap.getOrElse(__fields.get(3), Nil).asInstanceOf[_root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.matchmakermultipaxos.Bootstrap] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.matchmakermultipaxos.Bootstrap(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MatchmakerMultiPaxosProto.javaDescriptor.getMessageTypes.get(34)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MatchmakerMultiPaxosProto.scalaDescriptor.messages(34)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 4 => __out = frankenpaxos.matchmakermultipaxos.Configuration
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.matchmakermultipaxos.Bootstrap(
    epoch = 0,
    reconfigurerIndex = 0,
    gcWatermark = 0
  )
  implicit class BootstrapLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakermultipaxos.Bootstrap]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.matchmakermultipaxos.Bootstrap](_l) {
    def epoch: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.epoch)((c_, f_) => c_.copy(epoch = f_))
    def reconfigurerIndex: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.reconfigurerIndex)((c_, f_) => c_.copy(reconfigurerIndex = f_))
    def gcWatermark: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.gcWatermark)((c_, f_) => c_.copy(gcWatermark = f_))
    def configuration: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[frankenpaxos.matchmakermultipaxos.Configuration]] = field(_.configuration)((c_, f_) => c_.copy(configuration = f_))
  }
  final val EPOCH_FIELD_NUMBER = 1
  final val RECONFIGURER_INDEX_FIELD_NUMBER = 2
  final val GCWATERMARK_FIELD_NUMBER = 3
  final val CONFIGURATION_FIELD_NUMBER = 4
}
