// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.fastmultipaxos

@SerialVersionUID(0L)
final case class AcceptorInbound(
    request: frankenpaxos.fastmultipaxos.AcceptorInbound.Request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[AcceptorInbound] with scalapb.lenses.Updatable[AcceptorInbound] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (request.proposeRequest.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.proposeRequest.get.serializedSize) + request.proposeRequest.get.serializedSize }
      if (request.phase1A.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.phase1A.get.serializedSize) + request.phase1A.get.serializedSize }
      if (request.phase2A.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.phase2A.get.serializedSize) + request.phase2A.get.serializedSize }
      if (request.phase2ABuffer.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.phase2ABuffer.get.serializedSize) + request.phase2ABuffer.get.serializedSize }
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
      request.proposeRequest.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      request.phase1A.foreach { __v =>
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      request.phase2A.foreach { __v =>
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      request.phase2ABuffer.foreach { __v =>
        _output__.writeTag(4, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.fastmultipaxos.AcceptorInbound = {
      var __request = this.request
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.ProposeRequest(_root_.scalapb.LiteParser.readMessage(_input__, request.proposeRequest.getOrElse(frankenpaxos.fastmultipaxos.ProposeRequest.defaultInstance)))
          case 18 =>
            __request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase1A(_root_.scalapb.LiteParser.readMessage(_input__, request.phase1A.getOrElse(frankenpaxos.fastmultipaxos.Phase1a.defaultInstance)))
          case 26 =>
            __request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2A(_root_.scalapb.LiteParser.readMessage(_input__, request.phase2A.getOrElse(frankenpaxos.fastmultipaxos.Phase2a.defaultInstance)))
          case 34 =>
            __request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2ABuffer(_root_.scalapb.LiteParser.readMessage(_input__, request.phase2ABuffer.getOrElse(frankenpaxos.fastmultipaxos.Phase2aBuffer.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.fastmultipaxos.AcceptorInbound(
          request = __request
      )
    }
    def getProposeRequest: frankenpaxos.fastmultipaxos.ProposeRequest = request.proposeRequest.getOrElse(frankenpaxos.fastmultipaxos.ProposeRequest.defaultInstance)
    def withProposeRequest(__v: frankenpaxos.fastmultipaxos.ProposeRequest): AcceptorInbound = copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.ProposeRequest(__v))
    def getPhase1A: frankenpaxos.fastmultipaxos.Phase1a = request.phase1A.getOrElse(frankenpaxos.fastmultipaxos.Phase1a.defaultInstance)
    def withPhase1A(__v: frankenpaxos.fastmultipaxos.Phase1a): AcceptorInbound = copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase1A(__v))
    def getPhase2A: frankenpaxos.fastmultipaxos.Phase2a = request.phase2A.getOrElse(frankenpaxos.fastmultipaxos.Phase2a.defaultInstance)
    def withPhase2A(__v: frankenpaxos.fastmultipaxos.Phase2a): AcceptorInbound = copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2A(__v))
    def getPhase2ABuffer: frankenpaxos.fastmultipaxos.Phase2aBuffer = request.phase2ABuffer.getOrElse(frankenpaxos.fastmultipaxos.Phase2aBuffer.defaultInstance)
    def withPhase2ABuffer(__v: frankenpaxos.fastmultipaxos.Phase2aBuffer): AcceptorInbound = copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2ABuffer(__v))
    def clearRequest: AcceptorInbound = copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Empty)
    def withRequest(__v: frankenpaxos.fastmultipaxos.AcceptorInbound.Request): AcceptorInbound = copy(request = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => request.proposeRequest.orNull
        case 2 => request.phase1A.orNull
        case 3 => request.phase2A.orNull
        case 4 => request.phase2ABuffer.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => request.proposeRequest.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => request.phase1A.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => request.phase2A.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 4 => request.phase2ABuffer.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.fastmultipaxos.AcceptorInbound
}

object AcceptorInbound extends scalapb.GeneratedMessageCompanion[frankenpaxos.fastmultipaxos.AcceptorInbound] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.fastmultipaxos.AcceptorInbound] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.fastmultipaxos.AcceptorInbound = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.fastmultipaxos.AcceptorInbound(
      request = __fieldsMap.get(__fields.get(0)).asInstanceOf[scala.Option[frankenpaxos.fastmultipaxos.ProposeRequest]].map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.ProposeRequest)
    .orElse[frankenpaxos.fastmultipaxos.AcceptorInbound.Request](__fieldsMap.get(__fields.get(1)).asInstanceOf[scala.Option[frankenpaxos.fastmultipaxos.Phase1a]].map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase1A))
    .orElse[frankenpaxos.fastmultipaxos.AcceptorInbound.Request](__fieldsMap.get(__fields.get(2)).asInstanceOf[scala.Option[frankenpaxos.fastmultipaxos.Phase2a]].map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2A))
    .orElse[frankenpaxos.fastmultipaxos.AcceptorInbound.Request](__fieldsMap.get(__fields.get(3)).asInstanceOf[scala.Option[frankenpaxos.fastmultipaxos.Phase2aBuffer]].map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2ABuffer))
    .getOrElse(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.fastmultipaxos.AcceptorInbound] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.fastmultipaxos.AcceptorInbound(
        request = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[scala.Option[frankenpaxos.fastmultipaxos.ProposeRequest]]).map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.ProposeRequest)
    .orElse[frankenpaxos.fastmultipaxos.AcceptorInbound.Request](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[scala.Option[frankenpaxos.fastmultipaxos.Phase1a]]).map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase1A))
    .orElse[frankenpaxos.fastmultipaxos.AcceptorInbound.Request](__fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[scala.Option[frankenpaxos.fastmultipaxos.Phase2a]]).map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2A))
    .orElse[frankenpaxos.fastmultipaxos.AcceptorInbound.Request](__fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[scala.Option[frankenpaxos.fastmultipaxos.Phase2aBuffer]]).map(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2ABuffer))
    .getOrElse(frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = FastMultiPaxosProto.javaDescriptor.getMessageTypes.get(19)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = FastMultiPaxosProto.scalaDescriptor.messages(19)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.fastmultipaxos.ProposeRequest
      case 2 => __out = frankenpaxos.fastmultipaxos.Phase1a
      case 3 => __out = frankenpaxos.fastmultipaxos.Phase2a
      case 4 => __out = frankenpaxos.fastmultipaxos.Phase2aBuffer
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.fastmultipaxos.AcceptorInbound(
  )
  sealed trait Request extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isProposeRequest: _root_.scala.Boolean = false
    def isPhase1A: _root_.scala.Boolean = false
    def isPhase2A: _root_.scala.Boolean = false
    def isPhase2ABuffer: _root_.scala.Boolean = false
    def proposeRequest: scala.Option[frankenpaxos.fastmultipaxos.ProposeRequest] = None
    def phase1A: scala.Option[frankenpaxos.fastmultipaxos.Phase1a] = None
    def phase2A: scala.Option[frankenpaxos.fastmultipaxos.Phase2a] = None
    def phase2ABuffer: scala.Option[frankenpaxos.fastmultipaxos.Phase2aBuffer] = None
  }
  object Request extends {
    @SerialVersionUID(0L)
    case object Empty extends frankenpaxos.fastmultipaxos.AcceptorInbound.Request {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class ProposeRequest(value: frankenpaxos.fastmultipaxos.ProposeRequest) extends frankenpaxos.fastmultipaxos.AcceptorInbound.Request {
      type ValueType = frankenpaxos.fastmultipaxos.ProposeRequest
      override def isProposeRequest: _root_.scala.Boolean = true
      override def proposeRequest: scala.Option[frankenpaxos.fastmultipaxos.ProposeRequest] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class Phase1A(value: frankenpaxos.fastmultipaxos.Phase1a) extends frankenpaxos.fastmultipaxos.AcceptorInbound.Request {
      type ValueType = frankenpaxos.fastmultipaxos.Phase1a
      override def isPhase1A: _root_.scala.Boolean = true
      override def phase1A: scala.Option[frankenpaxos.fastmultipaxos.Phase1a] = Some(value)
      override def number: _root_.scala.Int = 2
    }
    @SerialVersionUID(0L)
    final case class Phase2A(value: frankenpaxos.fastmultipaxos.Phase2a) extends frankenpaxos.fastmultipaxos.AcceptorInbound.Request {
      type ValueType = frankenpaxos.fastmultipaxos.Phase2a
      override def isPhase2A: _root_.scala.Boolean = true
      override def phase2A: scala.Option[frankenpaxos.fastmultipaxos.Phase2a] = Some(value)
      override def number: _root_.scala.Int = 3
    }
    @SerialVersionUID(0L)
    final case class Phase2ABuffer(value: frankenpaxos.fastmultipaxos.Phase2aBuffer) extends frankenpaxos.fastmultipaxos.AcceptorInbound.Request {
      type ValueType = frankenpaxos.fastmultipaxos.Phase2aBuffer
      override def isPhase2ABuffer: _root_.scala.Boolean = true
      override def phase2ABuffer: scala.Option[frankenpaxos.fastmultipaxos.Phase2aBuffer] = Some(value)
      override def number: _root_.scala.Int = 4
    }
  }
  implicit class AcceptorInboundLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fastmultipaxos.AcceptorInbound]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.fastmultipaxos.AcceptorInbound](_l) {
    def proposeRequest: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fastmultipaxos.ProposeRequest] = field(_.getProposeRequest)((c_, f_) => c_.copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.ProposeRequest(f_)))
    def phase1A: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fastmultipaxos.Phase1a] = field(_.getPhase1A)((c_, f_) => c_.copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase1A(f_)))
    def phase2A: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fastmultipaxos.Phase2a] = field(_.getPhase2A)((c_, f_) => c_.copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2A(f_)))
    def phase2ABuffer: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fastmultipaxos.Phase2aBuffer] = field(_.getPhase2ABuffer)((c_, f_) => c_.copy(request = frankenpaxos.fastmultipaxos.AcceptorInbound.Request.Phase2ABuffer(f_)))
    def request: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.fastmultipaxos.AcceptorInbound.Request] = field(_.request)((c_, f_) => c_.copy(request = f_))
  }
  final val PROPOSE_REQUEST_FIELD_NUMBER = 1
  final val PHASE1A_FIELD_NUMBER = 2
  final val PHASE2A_FIELD_NUMBER = 3
  final val PHASE2A_BUFFER_FIELD_NUMBER = 4
}
