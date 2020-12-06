// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.raftquorum

/** Reads specific to LRQR
  */
@SerialVersionUID(0L)
final case class ClientQuorumQuery(
    query: frankenpaxos.raft.ReadCommand
    ) extends scalapb.GeneratedMessage with scalapb.Message[ClientQuorumQuery] with scalapb.lenses.Updatable[ClientQuorumQuery] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(query.serializedSize) + query.serializedSize
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
      _output__.writeTag(1, 2)
      _output__.writeUInt32NoTag(query.serializedSize)
      query.writeTo(_output__)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.raftquorum.ClientQuorumQuery = {
      var __query = this.query
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __query = _root_.scalapb.LiteParser.readMessage(_input__, __query)
            __requiredFields0 &= 0xfffffffffffffffeL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.raftquorum.ClientQuorumQuery(
          query = __query
      )
    }
    def withQuery(__v: frankenpaxos.raft.ReadCommand): ClientQuorumQuery = copy(query = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => query
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => query.toPMessage
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.raftquorum.ClientQuorumQuery
}

object ClientQuorumQuery extends scalapb.GeneratedMessageCompanion[frankenpaxos.raftquorum.ClientQuorumQuery] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.raftquorum.ClientQuorumQuery] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.raftquorum.ClientQuorumQuery = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.raftquorum.ClientQuorumQuery(
      __fieldsMap(__fields.get(0)).asInstanceOf[frankenpaxos.raft.ReadCommand]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.raftquorum.ClientQuorumQuery] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.raftquorum.ClientQuorumQuery(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[frankenpaxos.raft.ReadCommand]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RaftQuorumProto.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RaftQuorumProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.raft.ReadCommand
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.raftquorum.ClientQuorumQuery(
    query = frankenpaxos.raft.ReadCommand.defaultInstance
  )
  implicit class ClientQuorumQueryLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raftquorum.ClientQuorumQuery]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.raftquorum.ClientQuorumQuery](_l) {
    def query: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raft.ReadCommand] = field(_.query)((c_, f_) => c_.copy(query = f_))
  }
  final val QUERY_FIELD_NUMBER = 1
}
