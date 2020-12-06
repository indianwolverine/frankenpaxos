// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.simplebpaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class DepServiceNodeInbound(
    request: frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request = frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.Empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[DepServiceNodeInbound] with scalapb.lenses.Updatable[DepServiceNodeInbound] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (request.dependencyRequest.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.dependencyRequest.get.serializedSize) + request.dependencyRequest.get.serializedSize }
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
      request.dependencyRequest.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.simplebpaxos.DepServiceNodeInbound = {
      var __request = this.request
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __request = frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.DependencyRequest(_root_.scalapb.LiteParser.readMessage(_input__, request.dependencyRequest.getOrElse(frankenpaxos.simplebpaxos.DependencyRequest.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.simplebpaxos.DepServiceNodeInbound(
          request = __request
      )
    }
    def getDependencyRequest: frankenpaxos.simplebpaxos.DependencyRequest = request.dependencyRequest.getOrElse(frankenpaxos.simplebpaxos.DependencyRequest.defaultInstance)
    def withDependencyRequest(__v: frankenpaxos.simplebpaxos.DependencyRequest): DepServiceNodeInbound = copy(request = frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.DependencyRequest(__v))
    def clearRequest: DepServiceNodeInbound = copy(request = frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.Empty)
    def withRequest(__v: frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request): DepServiceNodeInbound = copy(request = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => request.dependencyRequest.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => request.dependencyRequest.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.simplebpaxos.DepServiceNodeInbound
}

object DepServiceNodeInbound extends scalapb.GeneratedMessageCompanion[frankenpaxos.simplebpaxos.DepServiceNodeInbound] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.simplebpaxos.DepServiceNodeInbound] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.simplebpaxos.DepServiceNodeInbound = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.simplebpaxos.DepServiceNodeInbound(
      request = __fieldsMap.get(__fields.get(0)).asInstanceOf[scala.Option[frankenpaxos.simplebpaxos.DependencyRequest]].map(frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.DependencyRequest)
    .getOrElse(frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.simplebpaxos.DepServiceNodeInbound] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.simplebpaxos.DepServiceNodeInbound(
        request = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[scala.Option[frankenpaxos.simplebpaxos.DependencyRequest]]).map(frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.DependencyRequest)
    .getOrElse(frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SimpleBPaxosProto.javaDescriptor.getMessageTypes.get(19)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SimpleBPaxosProto.scalaDescriptor.messages(19)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.simplebpaxos.DependencyRequest
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.simplebpaxos.DepServiceNodeInbound(
  )
  sealed trait Request extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isDependencyRequest: _root_.scala.Boolean = false
    def dependencyRequest: scala.Option[frankenpaxos.simplebpaxos.DependencyRequest] = None
  }
  object Request extends {
    @SerialVersionUID(0L)
    case object Empty extends frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class DependencyRequest(value: frankenpaxos.simplebpaxos.DependencyRequest) extends frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request {
      type ValueType = frankenpaxos.simplebpaxos.DependencyRequest
      override def isDependencyRequest: _root_.scala.Boolean = true
      override def dependencyRequest: scala.Option[frankenpaxos.simplebpaxos.DependencyRequest] = Some(value)
      override def number: _root_.scala.Int = 1
    }
  }
  implicit class DepServiceNodeInboundLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplebpaxos.DepServiceNodeInbound]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.simplebpaxos.DepServiceNodeInbound](_l) {
    def dependencyRequest: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplebpaxos.DependencyRequest] = field(_.getDependencyRequest)((c_, f_) => c_.copy(request = frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request.DependencyRequest(f_)))
    def request: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplebpaxos.DepServiceNodeInbound.Request] = field(_.request)((c_, f_) => c_.copy(request = f_))
  }
  final val DEPENDENCY_REQUEST_FIELD_NUMBER = 1
}
