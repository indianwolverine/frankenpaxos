// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.simplegcbpaxos

/** A client issued command.
  *
  * @param clientAddress
  *   The client's address.
  * @param clientPseudonym
  *   Clients use pseudonyms to simulate multiple clients. See Fast MultiPaxos
  *   for more explanation.
  * @param clientId
  *   Clients annotate every command with a unique and monotonically increasing
  *   id. The pair of (client address, client id) uniquely identify a command.
  * @param command
  *   The actual command.
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Command(
    clientAddress: _root_.com.google.protobuf.ByteString,
    clientPseudonym: _root_.scala.Int,
    clientId: _root_.scala.Int,
    command: _root_.com.google.protobuf.ByteString
    ) extends scalapb.GeneratedMessage with scalapb.Message[Command] with scalapb.lenses.Updatable[Command] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(1, clientAddress)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, clientPseudonym)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(3, clientId)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(4, command)
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
      _output__.writeBytes(1, clientAddress)
      _output__.writeInt32(2, clientPseudonym)
      _output__.writeInt32(3, clientId)
      _output__.writeBytes(4, command)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.simplegcbpaxos.Command = {
      var __clientAddress = this.clientAddress
      var __clientPseudonym = this.clientPseudonym
      var __clientId = this.clientId
      var __command = this.command
      var __requiredFields0: _root_.scala.Long = 0xfL
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __clientAddress = _input__.readBytes()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __clientPseudonym = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case 24 =>
            __clientId = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffbL
          case 34 =>
            __command = _input__.readBytes()
            __requiredFields0 &= 0xfffffffffffffff7L
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.simplegcbpaxos.Command(
          clientAddress = __clientAddress,
          clientPseudonym = __clientPseudonym,
          clientId = __clientId,
          command = __command
      )
    }
    def withClientAddress(__v: _root_.com.google.protobuf.ByteString): Command = copy(clientAddress = __v)
    def withClientPseudonym(__v: _root_.scala.Int): Command = copy(clientPseudonym = __v)
    def withClientId(__v: _root_.scala.Int): Command = copy(clientId = __v)
    def withCommand(__v: _root_.com.google.protobuf.ByteString): Command = copy(command = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => clientAddress
        case 2 => clientPseudonym
        case 3 => clientId
        case 4 => command
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PByteString(clientAddress)
        case 2 => _root_.scalapb.descriptors.PInt(clientPseudonym)
        case 3 => _root_.scalapb.descriptors.PInt(clientId)
        case 4 => _root_.scalapb.descriptors.PByteString(command)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.simplegcbpaxos.Command
}

object Command extends scalapb.GeneratedMessageCompanion[frankenpaxos.simplegcbpaxos.Command] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.simplegcbpaxos.Command] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.simplegcbpaxos.Command = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.simplegcbpaxos.Command(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.com.google.protobuf.ByteString],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(2)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(3)).asInstanceOf[_root_.com.google.protobuf.ByteString]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.simplegcbpaxos.Command] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.simplegcbpaxos.Command(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.com.google.protobuf.ByteString],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).get.as[_root_.com.google.protobuf.ByteString]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SimpleGcBPaxosProto.javaDescriptor.getMessageTypes.get(3)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SimpleGcBPaxosProto.scalaDescriptor.messages(3)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.simplegcbpaxos.Command(
    clientAddress = _root_.com.google.protobuf.ByteString.EMPTY,
    clientPseudonym = 0,
    clientId = 0,
    command = _root_.com.google.protobuf.ByteString.EMPTY
  )
  implicit class CommandLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplegcbpaxos.Command]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.simplegcbpaxos.Command](_l) {
    def clientAddress: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.clientAddress)((c_, f_) => c_.copy(clientAddress = f_))
    def clientPseudonym: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.clientPseudonym)((c_, f_) => c_.copy(clientPseudonym = f_))
    def clientId: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.clientId)((c_, f_) => c_.copy(clientId = f_))
    def command: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.command)((c_, f_) => c_.copy(command = f_))
  }
  final val CLIENT_ADDRESS_FIELD_NUMBER = 1
  final val CLIENT_PSEUDONYM_FIELD_NUMBER = 2
  final val CLIENT_ID_FIELD_NUMBER = 3
  final val COMMAND_FIELD_NUMBER = 4
}
