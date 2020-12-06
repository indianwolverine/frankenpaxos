// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.batchedunreplicated

/** Protocol messages. //////////////////////////////////////////////////////////
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class ClientRequest(
    command: frankenpaxos.batchedunreplicated.Command
    ) extends scalapb.GeneratedMessage with scalapb.Message[ClientRequest] with scalapb.lenses.Updatable[ClientRequest] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(command.serializedSize) + command.serializedSize
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
      _output__.writeUInt32NoTag(command.serializedSize)
      command.writeTo(_output__)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.batchedunreplicated.ClientRequest = {
      var __command = this.command
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __command = _root_.scalapb.LiteParser.readMessage(_input__, __command)
            __requiredFields0 &= 0xfffffffffffffffeL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.batchedunreplicated.ClientRequest(
          command = __command
      )
    }
    def withCommand(__v: frankenpaxos.batchedunreplicated.Command): ClientRequest = copy(command = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => command
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => command.toPMessage
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.batchedunreplicated.ClientRequest
}

object ClientRequest extends scalapb.GeneratedMessageCompanion[frankenpaxos.batchedunreplicated.ClientRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.batchedunreplicated.ClientRequest] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.batchedunreplicated.ClientRequest = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.batchedunreplicated.ClientRequest(
      __fieldsMap(__fields.get(0)).asInstanceOf[frankenpaxos.batchedunreplicated.Command]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.batchedunreplicated.ClientRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.batchedunreplicated.ClientRequest(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[frankenpaxos.batchedunreplicated.Command]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = BatchedUnreplicatedProto.javaDescriptor.getMessageTypes.get(2)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = BatchedUnreplicatedProto.scalaDescriptor.messages(2)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.batchedunreplicated.Command
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.batchedunreplicated.ClientRequest(
    command = frankenpaxos.batchedunreplicated.Command.defaultInstance
  )
  implicit class ClientRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.batchedunreplicated.ClientRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.batchedunreplicated.ClientRequest](_l) {
    def command: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.batchedunreplicated.Command] = field(_.command)((c_, f_) => c_.copy(command = f_))
  }
  final val COMMAND_FIELD_NUMBER = 1
}
