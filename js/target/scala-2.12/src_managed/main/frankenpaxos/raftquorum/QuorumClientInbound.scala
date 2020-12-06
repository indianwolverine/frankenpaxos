// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.raftquorum

@SerialVersionUID(0L)
final case class QuorumClientInbound(
    request: frankenpaxos.raftquorum.QuorumClientInbound.Request = frankenpaxos.raftquorum.QuorumClientInbound.Request.Empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[QuorumClientInbound] with scalapb.lenses.Updatable[QuorumClientInbound] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (request.clientRequestResponse.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.clientRequestResponse.get.serializedSize) + request.clientRequestResponse.get.serializedSize }
      if (request.clientQuorumQueryResponse.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.clientQuorumQueryResponse.get.serializedSize) + request.clientQuorumQueryResponse.get.serializedSize }
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
      request.clientRequestResponse.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      request.clientQuorumQueryResponse.foreach { __v =>
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.raftquorum.QuorumClientInbound = {
      var __request = this.request
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __request = frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientRequestResponse(_root_.scalapb.LiteParser.readMessage(_input__, request.clientRequestResponse.getOrElse(frankenpaxos.raft.ClientRequestResponse.defaultInstance)))
          case 18 =>
            __request = frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientQuorumQueryResponse(_root_.scalapb.LiteParser.readMessage(_input__, request.clientQuorumQueryResponse.getOrElse(frankenpaxos.raftquorum.ClientQuorumQueryResponse.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.raftquorum.QuorumClientInbound(
          request = __request
      )
    }
    def getClientRequestResponse: frankenpaxos.raft.ClientRequestResponse = request.clientRequestResponse.getOrElse(frankenpaxos.raft.ClientRequestResponse.defaultInstance)
    def withClientRequestResponse(__v: frankenpaxos.raft.ClientRequestResponse): QuorumClientInbound = copy(request = frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientRequestResponse(__v))
    def getClientQuorumQueryResponse: frankenpaxos.raftquorum.ClientQuorumQueryResponse = request.clientQuorumQueryResponse.getOrElse(frankenpaxos.raftquorum.ClientQuorumQueryResponse.defaultInstance)
    def withClientQuorumQueryResponse(__v: frankenpaxos.raftquorum.ClientQuorumQueryResponse): QuorumClientInbound = copy(request = frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientQuorumQueryResponse(__v))
    def clearRequest: QuorumClientInbound = copy(request = frankenpaxos.raftquorum.QuorumClientInbound.Request.Empty)
    def withRequest(__v: frankenpaxos.raftquorum.QuorumClientInbound.Request): QuorumClientInbound = copy(request = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => request.clientRequestResponse.orNull
        case 2 => request.clientQuorumQueryResponse.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => request.clientRequestResponse.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => request.clientQuorumQueryResponse.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.raftquorum.QuorumClientInbound
}

object QuorumClientInbound extends scalapb.GeneratedMessageCompanion[frankenpaxos.raftquorum.QuorumClientInbound] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.raftquorum.QuorumClientInbound] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.raftquorum.QuorumClientInbound = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.raftquorum.QuorumClientInbound(
      request = __fieldsMap.get(__fields.get(0)).asInstanceOf[scala.Option[frankenpaxos.raft.ClientRequestResponse]].map(frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientRequestResponse)
    .orElse[frankenpaxos.raftquorum.QuorumClientInbound.Request](__fieldsMap.get(__fields.get(1)).asInstanceOf[scala.Option[frankenpaxos.raftquorum.ClientQuorumQueryResponse]].map(frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientQuorumQueryResponse))
    .getOrElse(frankenpaxos.raftquorum.QuorumClientInbound.Request.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.raftquorum.QuorumClientInbound] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.raftquorum.QuorumClientInbound(
        request = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[scala.Option[frankenpaxos.raft.ClientRequestResponse]]).map(frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientRequestResponse)
    .orElse[frankenpaxos.raftquorum.QuorumClientInbound.Request](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[scala.Option[frankenpaxos.raftquorum.ClientQuorumQueryResponse]]).map(frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientQuorumQueryResponse))
    .getOrElse(frankenpaxos.raftquorum.QuorumClientInbound.Request.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RaftQuorumProto.javaDescriptor.getMessageTypes.get(3)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RaftQuorumProto.scalaDescriptor.messages(3)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.raft.ClientRequestResponse
      case 2 => __out = frankenpaxos.raftquorum.ClientQuorumQueryResponse
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.raftquorum.QuorumClientInbound(
  )
  sealed trait Request extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isClientRequestResponse: _root_.scala.Boolean = false
    def isClientQuorumQueryResponse: _root_.scala.Boolean = false
    def clientRequestResponse: scala.Option[frankenpaxos.raft.ClientRequestResponse] = None
    def clientQuorumQueryResponse: scala.Option[frankenpaxos.raftquorum.ClientQuorumQueryResponse] = None
  }
  object Request extends {
    @SerialVersionUID(0L)
    case object Empty extends frankenpaxos.raftquorum.QuorumClientInbound.Request {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class ClientRequestResponse(value: frankenpaxos.raft.ClientRequestResponse) extends frankenpaxos.raftquorum.QuorumClientInbound.Request {
      type ValueType = frankenpaxos.raft.ClientRequestResponse
      override def isClientRequestResponse: _root_.scala.Boolean = true
      override def clientRequestResponse: scala.Option[frankenpaxos.raft.ClientRequestResponse] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class ClientQuorumQueryResponse(value: frankenpaxos.raftquorum.ClientQuorumQueryResponse) extends frankenpaxos.raftquorum.QuorumClientInbound.Request {
      type ValueType = frankenpaxos.raftquorum.ClientQuorumQueryResponse
      override def isClientQuorumQueryResponse: _root_.scala.Boolean = true
      override def clientQuorumQueryResponse: scala.Option[frankenpaxos.raftquorum.ClientQuorumQueryResponse] = Some(value)
      override def number: _root_.scala.Int = 2
    }
  }
  implicit class QuorumClientInboundLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raftquorum.QuorumClientInbound]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.raftquorum.QuorumClientInbound](_l) {
    def clientRequestResponse: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raft.ClientRequestResponse] = field(_.getClientRequestResponse)((c_, f_) => c_.copy(request = frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientRequestResponse(f_)))
    def clientQuorumQueryResponse: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raftquorum.ClientQuorumQueryResponse] = field(_.getClientQuorumQueryResponse)((c_, f_) => c_.copy(request = frankenpaxos.raftquorum.QuorumClientInbound.Request.ClientQuorumQueryResponse(f_)))
    def request: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.raftquorum.QuorumClientInbound.Request] = field(_.request)((c_, f_) => c_.copy(request = f_))
  }
  final val CLIENT_REQUEST_RESPONSE_FIELD_NUMBER = 1
  final val CLIENT_QUORUM_QUERY_RESPONSE_FIELD_NUMBER = 2
}
