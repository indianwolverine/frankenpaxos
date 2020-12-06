// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.mencius

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class ProxyReplicaInbound(
    request: frankenpaxos.mencius.ProxyReplicaInbound.Request = frankenpaxos.mencius.ProxyReplicaInbound.Request.Empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[ProxyReplicaInbound] with scalapb.lenses.Updatable[ProxyReplicaInbound] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (request.clientReplyBatch.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.clientReplyBatch.get.serializedSize) + request.clientReplyBatch.get.serializedSize }
      if (request.chosenWatermark.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.chosenWatermark.get.serializedSize) + request.chosenWatermark.get.serializedSize }
      if (request.recover.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(request.recover.get.serializedSize) + request.recover.get.serializedSize }
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
      request.clientReplyBatch.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      request.chosenWatermark.foreach { __v =>
        _output__.writeTag(2, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
      request.recover.foreach { __v =>
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.mencius.ProxyReplicaInbound = {
      var __request = this.request
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __request = frankenpaxos.mencius.ProxyReplicaInbound.Request.ClientReplyBatch(_root_.scalapb.LiteParser.readMessage(_input__, request.clientReplyBatch.getOrElse(frankenpaxos.mencius.ClientReplyBatch.defaultInstance)))
          case 18 =>
            __request = frankenpaxos.mencius.ProxyReplicaInbound.Request.ChosenWatermark(_root_.scalapb.LiteParser.readMessage(_input__, request.chosenWatermark.getOrElse(frankenpaxos.mencius.ChosenWatermark.defaultInstance)))
          case 26 =>
            __request = frankenpaxos.mencius.ProxyReplicaInbound.Request.Recover(_root_.scalapb.LiteParser.readMessage(_input__, request.recover.getOrElse(frankenpaxos.mencius.Recover.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.mencius.ProxyReplicaInbound(
          request = __request
      )
    }
    def getClientReplyBatch: frankenpaxos.mencius.ClientReplyBatch = request.clientReplyBatch.getOrElse(frankenpaxos.mencius.ClientReplyBatch.defaultInstance)
    def withClientReplyBatch(__v: frankenpaxos.mencius.ClientReplyBatch): ProxyReplicaInbound = copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.ClientReplyBatch(__v))
    def getChosenWatermark: frankenpaxos.mencius.ChosenWatermark = request.chosenWatermark.getOrElse(frankenpaxos.mencius.ChosenWatermark.defaultInstance)
    def withChosenWatermark(__v: frankenpaxos.mencius.ChosenWatermark): ProxyReplicaInbound = copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.ChosenWatermark(__v))
    def getRecover: frankenpaxos.mencius.Recover = request.recover.getOrElse(frankenpaxos.mencius.Recover.defaultInstance)
    def withRecover(__v: frankenpaxos.mencius.Recover): ProxyReplicaInbound = copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.Recover(__v))
    def clearRequest: ProxyReplicaInbound = copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.Empty)
    def withRequest(__v: frankenpaxos.mencius.ProxyReplicaInbound.Request): ProxyReplicaInbound = copy(request = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => request.clientReplyBatch.orNull
        case 2 => request.chosenWatermark.orNull
        case 3 => request.recover.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => request.clientReplyBatch.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => request.chosenWatermark.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 3 => request.recover.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.mencius.ProxyReplicaInbound
}

object ProxyReplicaInbound extends scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.ProxyReplicaInbound] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.ProxyReplicaInbound] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.mencius.ProxyReplicaInbound = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.mencius.ProxyReplicaInbound(
      request = __fieldsMap.get(__fields.get(0)).asInstanceOf[scala.Option[frankenpaxos.mencius.ClientReplyBatch]].map(frankenpaxos.mencius.ProxyReplicaInbound.Request.ClientReplyBatch)
    .orElse[frankenpaxos.mencius.ProxyReplicaInbound.Request](__fieldsMap.get(__fields.get(1)).asInstanceOf[scala.Option[frankenpaxos.mencius.ChosenWatermark]].map(frankenpaxos.mencius.ProxyReplicaInbound.Request.ChosenWatermark))
    .orElse[frankenpaxos.mencius.ProxyReplicaInbound.Request](__fieldsMap.get(__fields.get(2)).asInstanceOf[scala.Option[frankenpaxos.mencius.Recover]].map(frankenpaxos.mencius.ProxyReplicaInbound.Request.Recover))
    .getOrElse(frankenpaxos.mencius.ProxyReplicaInbound.Request.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.mencius.ProxyReplicaInbound] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.mencius.ProxyReplicaInbound(
        request = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[scala.Option[frankenpaxos.mencius.ClientReplyBatch]]).map(frankenpaxos.mencius.ProxyReplicaInbound.Request.ClientReplyBatch)
    .orElse[frankenpaxos.mencius.ProxyReplicaInbound.Request](__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).flatMap(_.as[scala.Option[frankenpaxos.mencius.ChosenWatermark]]).map(frankenpaxos.mencius.ProxyReplicaInbound.Request.ChosenWatermark))
    .orElse[frankenpaxos.mencius.ProxyReplicaInbound.Request](__fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[scala.Option[frankenpaxos.mencius.Recover]]).map(frankenpaxos.mencius.ProxyReplicaInbound.Request.Recover))
    .getOrElse(frankenpaxos.mencius.ProxyReplicaInbound.Request.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MenciusProto.javaDescriptor.getMessageTypes.get(34)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MenciusProto.scalaDescriptor.messages(34)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.mencius.ClientReplyBatch
      case 2 => __out = frankenpaxos.mencius.ChosenWatermark
      case 3 => __out = frankenpaxos.mencius.Recover
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.mencius.ProxyReplicaInbound(
  )
  sealed trait Request extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isClientReplyBatch: _root_.scala.Boolean = false
    def isChosenWatermark: _root_.scala.Boolean = false
    def isRecover: _root_.scala.Boolean = false
    def clientReplyBatch: scala.Option[frankenpaxos.mencius.ClientReplyBatch] = None
    def chosenWatermark: scala.Option[frankenpaxos.mencius.ChosenWatermark] = None
    def recover: scala.Option[frankenpaxos.mencius.Recover] = None
  }
  object Request extends {
    @SerialVersionUID(0L)
    case object Empty extends frankenpaxos.mencius.ProxyReplicaInbound.Request {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class ClientReplyBatch(value: frankenpaxos.mencius.ClientReplyBatch) extends frankenpaxos.mencius.ProxyReplicaInbound.Request {
      type ValueType = frankenpaxos.mencius.ClientReplyBatch
      override def isClientReplyBatch: _root_.scala.Boolean = true
      override def clientReplyBatch: scala.Option[frankenpaxos.mencius.ClientReplyBatch] = Some(value)
      override def number: _root_.scala.Int = 1
    }
    @SerialVersionUID(0L)
    final case class ChosenWatermark(value: frankenpaxos.mencius.ChosenWatermark) extends frankenpaxos.mencius.ProxyReplicaInbound.Request {
      type ValueType = frankenpaxos.mencius.ChosenWatermark
      override def isChosenWatermark: _root_.scala.Boolean = true
      override def chosenWatermark: scala.Option[frankenpaxos.mencius.ChosenWatermark] = Some(value)
      override def number: _root_.scala.Int = 2
    }
    @SerialVersionUID(0L)
    final case class Recover(value: frankenpaxos.mencius.Recover) extends frankenpaxos.mencius.ProxyReplicaInbound.Request {
      type ValueType = frankenpaxos.mencius.Recover
      override def isRecover: _root_.scala.Boolean = true
      override def recover: scala.Option[frankenpaxos.mencius.Recover] = Some(value)
      override def number: _root_.scala.Int = 3
    }
  }
  implicit class ProxyReplicaInboundLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.ProxyReplicaInbound]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.mencius.ProxyReplicaInbound](_l) {
    def clientReplyBatch: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.ClientReplyBatch] = field(_.getClientReplyBatch)((c_, f_) => c_.copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.ClientReplyBatch(f_)))
    def chosenWatermark: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.ChosenWatermark] = field(_.getChosenWatermark)((c_, f_) => c_.copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.ChosenWatermark(f_)))
    def recover: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.Recover] = field(_.getRecover)((c_, f_) => c_.copy(request = frankenpaxos.mencius.ProxyReplicaInbound.Request.Recover(f_)))
    def request: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.ProxyReplicaInbound.Request] = field(_.request)((c_, f_) => c_.copy(request = f_))
  }
  final val CLIENT_REPLY_BATCH_FIELD_NUMBER = 1
  final val CHOSEN_WATERMARK_FIELD_NUMBER = 2
  final val RECOVER_FIELD_NUMBER = 3
}
