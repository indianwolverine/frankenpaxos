// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.simplebpaxos

@SerialVersionUID(0L)
final case class VertexIdPrefixSetProto(
    numLeaders: _root_.scala.Int,
    intPrefixSet: _root_.scala.collection.Seq[frankenpaxos.compact.IntPrefixSetProto] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[VertexIdPrefixSetProto] with scalapb.lenses.Updatable[VertexIdPrefixSetProto] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, numLeaders)
      intPrefixSet.foreach(intPrefixSet => __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(intPrefixSet.serializedSize) + intPrefixSet.serializedSize)
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
      _output__.writeInt32(1, numLeaders)
      intPrefixSet.foreach { __v =>
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.simplebpaxos.VertexIdPrefixSetProto = {
      var __numLeaders = this.numLeaders
      val __intPrefixSet = (_root_.scala.collection.immutable.Vector.newBuilder[frankenpaxos.compact.IntPrefixSetProto] ++= this.intPrefixSet)
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __numLeaders = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 18 =>
            __intPrefixSet += _root_.scalapb.LiteParser.readMessage(_input__, frankenpaxos.compact.IntPrefixSetProto.defaultInstance)
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.simplebpaxos.VertexIdPrefixSetProto(
          numLeaders = __numLeaders,
          intPrefixSet = __intPrefixSet.result()
      )
    }
    def withNumLeaders(__v: _root_.scala.Int): VertexIdPrefixSetProto = copy(numLeaders = __v)
    def clearIntPrefixSet = copy(intPrefixSet = _root_.scala.collection.Seq.empty)
    def addIntPrefixSet(__vs: frankenpaxos.compact.IntPrefixSetProto*): VertexIdPrefixSetProto = addAllIntPrefixSet(__vs)
    def addAllIntPrefixSet(__vs: TraversableOnce[frankenpaxos.compact.IntPrefixSetProto]): VertexIdPrefixSetProto = copy(intPrefixSet = intPrefixSet ++ __vs)
    def withIntPrefixSet(__v: _root_.scala.collection.Seq[frankenpaxos.compact.IntPrefixSetProto]): VertexIdPrefixSetProto = copy(intPrefixSet = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => numLeaders
        case 2 => intPrefixSet
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(numLeaders)
        case 2 => _root_.scalapb.descriptors.PRepeated(intPrefixSet.map(_.toPMessage)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.simplebpaxos.VertexIdPrefixSetProto
}

object VertexIdPrefixSetProto extends scalapb.GeneratedMessageCompanion[frankenpaxos.simplebpaxos.VertexIdPrefixSetProto] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.simplebpaxos.VertexIdPrefixSetProto] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.simplebpaxos.VertexIdPrefixSetProto = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.simplebpaxos.VertexIdPrefixSetProto(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap.getOrElse(__fields.get(1), Nil).asInstanceOf[_root_.scala.collection.Seq[frankenpaxos.compact.IntPrefixSetProto]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.simplebpaxos.VertexIdPrefixSetProto] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.simplebpaxos.VertexIdPrefixSetProto(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.collection.Seq[frankenpaxos.compact.IntPrefixSetProto]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SimpleBPaxosProto.javaDescriptor.getMessageTypes.get(5)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SimpleBPaxosProto.scalaDescriptor.messages(5)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = frankenpaxos.compact.IntPrefixSetProto
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.simplebpaxos.VertexIdPrefixSetProto(
    numLeaders = 0
  )
  implicit class VertexIdPrefixSetProtoLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplebpaxos.VertexIdPrefixSetProto]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.simplebpaxos.VertexIdPrefixSetProto](_l) {
    def numLeaders: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.numLeaders)((c_, f_) => c_.copy(numLeaders = f_))
    def intPrefixSet: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[frankenpaxos.compact.IntPrefixSetProto]] = field(_.intPrefixSet)((c_, f_) => c_.copy(intPrefixSet = f_))
  }
  final val NUMLEADERS_FIELD_NUMBER = 1
  final val INT_PREFIX_SET_FIELD_NUMBER = 2
}
