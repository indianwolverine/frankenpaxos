// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.fasterpaxos

/** @param chosenWatermark
  *   The leader knows that all entries in slots less than `chosenWatermark`
  *   have been chosen. Acceptors do not have to include slots below
  *   `chosenWatermark` in their phase1b response.
  *  
  *   The leader may know that some entries larger than `chosenWatermark` have
  *   also been chosen, but that's okay. It's not unsafe for acceptors to return
  *   too much information.
  * @param delegate
  *   The list of delegates (technically the indexes of the delegates) for this
  *   round.
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Phase1a(
    round: _root_.scala.Int,
    chosenWatermark: _root_.scala.Int,
    delegate: _root_.scala.collection.Seq[_root_.scala.Int] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[Phase1a] with scalapb.lenses.Updatable[Phase1a] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, round)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, chosenWatermark)
      delegate.foreach(delegate => __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(3, delegate))
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
      _output__.writeInt32(1, round)
      _output__.writeInt32(2, chosenWatermark)
      delegate.foreach { __v =>
        _output__.writeInt32(3, __v)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.fasterpaxos.Phase1a = {
      var __round = this.round
      var __chosenWatermark = this.chosenWatermark
      val __delegate = (_root_.scala.collection.immutable.Vector.newBuilder[_root_.scala.Int] ++= this.delegate)
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __round = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __chosenWatermark = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case 24 =>
            __delegate += _input__.readInt32()
          case 26 => {
            val length = _input__.readRawVarint32()
            val oldLimit = _input__.pushLimit(length)
            while (_input__.getBytesUntilLimit > 0) {
              __delegate += _input__.readInt32
            }
            _input__.popLimit(oldLimit)
          }
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.fasterpaxos.Phase1a(
          round = __round,
          chosenWatermark = __chosenWatermark,
          delegate = __delegate.result()
      )
    }
    def withRound(__v: _root_.scala.Int): Phase1a = copy(round = __v)
    def withChosenWatermark(__v: _root_.scala.Int): Phase1a = copy(chosenWatermark = __v)
    def clearDelegate = copy(delegate = _root_.scala.collection.Seq.empty)
    def addDelegate(__vs: _root_.scala.Int*): Phase1a = addAllDelegate(__vs)
    def addAllDelegate(__vs: TraversableOnce[_root_.scala.Int]): Phase1a = copy(delegate = delegate ++ __vs)
    def withDelegate(__v: _root_.scala.collection.Seq[_root_.scala.Int]): Phase1a = copy(delegate = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => round
        case 2 => chosenWatermark
        case 3 => delegate
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(round)
        case 2 => _root_.scalapb.descriptors.PInt(chosenWatermark)
        case 3 => _root_.scalapb.descriptors.PRepeated(delegate.map(_root_.scalapb.descriptors.PInt)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.fasterpaxos.Phase1a
}

object Phase1a extends scalapb.GeneratedMessageCompanion[frankenpaxos.fasterpaxos.Phase1a] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.fasterpaxos.Phase1a] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.fasterpaxos.Phase1a = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.fasterpaxos.Phase1a(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap.getOrElse(__fields.get(2), Nil).asInstanceOf[_root_.scala.collection.Seq[_root_.scala.Int]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.fasterpaxos.Phase1a] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.fasterpaxos.Phase1a(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.collection.Seq[_root_.scala.Int]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = FasterPaxosProto.javaDescriptor.getMessageTypes.get(6)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = FasterPaxosProto.scalaDescriptor.messages(6)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.fasterpaxos.Phase1a(
    round = 0,
    chosenWatermark = 0
  )
  implicit class Phase1aLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fasterpaxos.Phase1a]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.fasterpaxos.Phase1a](_l) {
    def round: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.round)((c_, f_) => c_.copy(round = f_))
    def chosenWatermark: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.chosenWatermark)((c_, f_) => c_.copy(chosenWatermark = f_))
    def delegate: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[_root_.scala.Int]] = field(_.delegate)((c_, f_) => c_.copy(delegate = f_))
  }
  final val ROUND_FIELD_NUMBER = 1
  final val CHOSEN_WATERMARK_FIELD_NUMBER = 2
  final val DELEGATE_FIELD_NUMBER = 3
}
