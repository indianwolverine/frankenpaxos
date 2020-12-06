// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.simplegcbpaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class ClientInbound(
    request: frankenpaxos.simplegcbpaxos.ClientInbound.Request = frankenpaxos.simplegcbpaxos.ClientInbound.Request.Empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[ClientInbound] with scalapb.lenses.Updatable[ClientInbound] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (request.clientReply.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.clientReply.get.serializedSize) + request.clientReply.get.serializedSize }
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
      request.clientReply.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.simplegcbpaxos.ClientInbound = {
      var __request = this.request
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __request = frankenpaxos.simplegcbpaxos.ClientInbound.Request.ClientReply(_root_.scalapb.LiteParser.readMessage(_input__, request.clientReply.getOrElse(frankenpaxos.simplegcbpaxos.ClientReply.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.simplegcbpaxos.ClientInbound(
          request = __request
      )
    }
    def getClientReply: frankenpaxos.simplegcbpaxos.ClientReply = request.clientReply.getOrElse(frankenpaxos.simplegcbpaxos.ClientReply.defaultInstance)
    def withClientReply(__v: frankenpaxos.simplegcbpaxos.ClientReply): ClientInbound = copy(request = frankenpaxos.simplegcbpaxos.ClientInbound.Request.ClientReply(__v))
    def clearRequest: ClientInbound = copy(request = frankenpaxos.simplegcbpaxos.ClientInbound.Request.Empty)
    def withRequest(__v: frankenpaxos.simplegcbpaxos.ClientInbound.Request): ClientInbound = copy(request = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => request.clientReply.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => request.clientReply.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.simplegcbpaxos.ClientInbound
}

object ClientInbound extends scalapb.GeneratedMessageCompanion[frankenpaxos.simplegcbpaxos.ClientInbound] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.simplegcbpaxos.ClientInbound] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.simplegcbpaxos.ClientInbound = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.simplegcbpaxos.ClientInbound(
      request = __fieldsMap.get(__fields.get(0)).asInstanceOf[scala.Option[frankenpaxos.simplegcbpaxos.ClientReply]].map(frankenpaxos.simplegcbpaxos.ClientInbound.Request.ClientReply)
    .getOrElse(frankenpaxos.simplegcbpaxos.ClientInbound.Request.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.simplegcbpaxos.ClientInbound] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.simplegcbpaxos.ClientInbound(
        request = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[scala.Option[frankenpaxos.simplegcbpaxos.ClientReply]]).map(frankenpaxos.simplegcbpaxos.ClientInbound.Request.ClientReply)
    .getOrElse(frankenpaxos.simplegcbpaxos.ClientInbound.Request.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SimpleGcBPaxosProto.javaDescriptor.getMessageTypes.get(29)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SimpleGcBPaxosProto.scalaDescriptor.messages(29)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.simplegcbpaxos.ClientReply
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.simplegcbpaxos.ClientInbound(
  )
  sealed trait Request extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isClientReply: _root_.scala.Boolean = false
    def clientReply: scala.Option[frankenpaxos.simplegcbpaxos.ClientReply] = None
  }
  object Request extends {
    @SerialVersionUID(0L)
    case object Empty extends frankenpaxos.simplegcbpaxos.ClientInbound.Request {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class ClientReply(value: frankenpaxos.simplegcbpaxos.ClientReply) extends frankenpaxos.simplegcbpaxos.ClientInbound.Request {
      type ValueType = frankenpaxos.simplegcbpaxos.ClientReply
      override def isClientReply: _root_.scala.Boolean = true
      override def clientReply: scala.Option[frankenpaxos.simplegcbpaxos.ClientReply] = Some(value)
      override def number: _root_.scala.Int = 1
    }
  }
  implicit class ClientInboundLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplegcbpaxos.ClientInbound]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.simplegcbpaxos.ClientInbound](_l) {
    def clientReply: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplegcbpaxos.ClientReply] = field(_.getClientReply)((c_, f_) => c_.copy(request = frankenpaxos.simplegcbpaxos.ClientInbound.Request.ClientReply(f_)))
    def request: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplegcbpaxos.ClientInbound.Request] = field(_.request)((c_, f_) => c_.copy(request = f_))
  }
  final val CLIENT_REPLY_FIELD_NUMBER = 1
}
