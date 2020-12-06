// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.matchmakerpaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class MatchRequest(
    acceptorGroup: frankenpaxos.matchmakerpaxos.AcceptorGroup
    ) extends scalapb.GeneratedMessage with scalapb.Message[MatchRequest] with scalapb.lenses.Updatable[MatchRequest] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(acceptorGroup.serializedSize) + acceptorGroup.serializedSize
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
      _output__.writeUInt32NoTag(acceptorGroup.serializedSize)
      acceptorGroup.writeTo(_output__)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.matchmakerpaxos.MatchRequest = {
      var __acceptorGroup = this.acceptorGroup
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __acceptorGroup = _root_.scalapb.LiteParser.readMessage(_input__, __acceptorGroup)
            __requiredFields0 &= 0xfffffffffffffffeL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.matchmakerpaxos.MatchRequest(
          acceptorGroup = __acceptorGroup
      )
    }
    def withAcceptorGroup(__v: frankenpaxos.matchmakerpaxos.AcceptorGroup): MatchRequest = copy(acceptorGroup = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => acceptorGroup
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => acceptorGroup.toPMessage
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.matchmakerpaxos.MatchRequest
}

object MatchRequest extends scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakerpaxos.MatchRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakerpaxos.MatchRequest] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.matchmakerpaxos.MatchRequest = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.matchmakerpaxos.MatchRequest(
      __fieldsMap(__fields.get(0)).asInstanceOf[frankenpaxos.matchmakerpaxos.AcceptorGroup]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.matchmakerpaxos.MatchRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.matchmakerpaxos.MatchRequest(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[frankenpaxos.matchmakerpaxos.AcceptorGroup]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MatchmakerPaxosProto.javaDescriptor.getMessageTypes.get(3)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MatchmakerPaxosProto.scalaDescriptor.messages(3)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.matchmakerpaxos.AcceptorGroup
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.matchmakerpaxos.MatchRequest(
    acceptorGroup = frankenpaxos.matchmakerpaxos.AcceptorGroup.defaultInstance
  )
  implicit class MatchRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakerpaxos.MatchRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.matchmakerpaxos.MatchRequest](_l) {
    def acceptorGroup: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakerpaxos.AcceptorGroup] = field(_.acceptorGroup)((c_, f_) => c_.copy(acceptorGroup = f_))
  }
  final val ACCEPTOR_GROUP_FIELD_NUMBER = 1
}
