// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.quorums

@SerialVersionUID(0L)
final case class SimpleMajorityProto(
    members: _root_.scala.collection.Seq[_root_.scala.Int] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[SimpleMajorityProto] with scalapb.lenses.Updatable[SimpleMajorityProto] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      members.foreach(members => __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, members))
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
      members.foreach { __v =>
        _output__.writeInt32(1, __v)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.quorums.SimpleMajorityProto = {
      val __members = (_root_.scala.collection.immutable.Vector.newBuilder[_root_.scala.Int] ++= this.members)
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __members += _input__.readInt32()
          case 10 => {
            val length = _input__.readRawVarint32()
            val oldLimit = _input__.pushLimit(length)
            while (_input__.getBytesUntilLimit > 0) {
              __members += _input__.readInt32
            }
            _input__.popLimit(oldLimit)
          }
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.quorums.SimpleMajorityProto(
          members = __members.result()
      )
    }
    def clearMembers = copy(members = _root_.scala.collection.Seq.empty)
    def addMembers(__vs: _root_.scala.Int*): SimpleMajorityProto = addAllMembers(__vs)
    def addAllMembers(__vs: TraversableOnce[_root_.scala.Int]): SimpleMajorityProto = copy(members = members ++ __vs)
    def withMembers(__v: _root_.scala.collection.Seq[_root_.scala.Int]): SimpleMajorityProto = copy(members = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => members
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PRepeated(members.map(_root_.scalapb.descriptors.PInt)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.quorums.SimpleMajorityProto
}

object SimpleMajorityProto extends scalapb.GeneratedMessageCompanion[frankenpaxos.quorums.SimpleMajorityProto] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.quorums.SimpleMajorityProto] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.quorums.SimpleMajorityProto = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.quorums.SimpleMajorityProto(
      __fieldsMap.getOrElse(__fields.get(0), Nil).asInstanceOf[_root_.scala.collection.Seq[_root_.scala.Int]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.quorums.SimpleMajorityProto] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.quorums.SimpleMajorityProto(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.collection.Seq[_root_.scala.Int]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = QuorumSystemProtoCompanion.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = QuorumSystemProtoCompanion.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.quorums.SimpleMajorityProto(
  )
  implicit class SimpleMajorityProtoLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.quorums.SimpleMajorityProto]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.quorums.SimpleMajorityProto](_l) {
    def members: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[_root_.scala.Int]] = field(_.members)((c_, f_) => c_.copy(members = f_))
  }
  final val MEMBERS_FIELD_NUMBER = 1
}
