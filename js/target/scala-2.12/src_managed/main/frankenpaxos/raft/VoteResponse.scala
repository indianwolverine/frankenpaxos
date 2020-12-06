// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.raft

@SerialVersionUID(0L)
final case class VoteResponse(
    term: _root_.scala.Int,
    voteGranted: _root_.scala.Boolean
    ) extends scalapb.GeneratedMessage with scalapb.Message[VoteResponse] with scalapb.lenses.Updatable[VoteResponse] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, term)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeBoolSize(2, voteGranted)
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
      _output__.writeInt32(1, term)
      _output__.writeBool(2, voteGranted)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.raft.VoteResponse = {
      var __term = this.term
      var __voteGranted = this.voteGranted
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __term = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __voteGranted = _input__.readBool()
            __requiredFields0 &= 0xfffffffffffffffdL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.raft.VoteResponse(
          term = __term,
          voteGranted = __voteGranted
      )
    }
    def withTerm(__v: _root_.scala.Int): VoteResponse = copy(term = __v)
    def withVoteGranted(__v: _root_.scala.Boolean): VoteResponse = copy(voteGranted = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => term
        case 2 => voteGranted
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(term)
        case 2 => _root_.scalapb.descriptors.PBoolean(voteGranted)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.raft.VoteResponse
}

object VoteResponse extends scalapb.GeneratedMessageCompanion[frankenpaxos.raft.VoteResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.raft.VoteResponse] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.raft.VoteResponse = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.raft.VoteResponse(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Boolean]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.raft.VoteResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.raft.VoteResponse(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Boolean]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RaftProto.javaDescriptor.getMessageTypes.get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RaftProto.scalaDescriptor.messages(1)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.raft.VoteResponse(
    term = 0,
    voteGranted = false
  )
  implicit class VoteResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raft.VoteResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.raft.VoteResponse](_l) {
    def term: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.term)((c_, f_) => c_.copy(term = f_))
    def voteGranted: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Boolean] = field(_.voteGranted)((c_, f_) => c_.copy(voteGranted = f_))
  }
  final val TERM_FIELD_NUMBER = 1
  final val VOTE_GRANTED_FIELD_NUMBER = 2
}
