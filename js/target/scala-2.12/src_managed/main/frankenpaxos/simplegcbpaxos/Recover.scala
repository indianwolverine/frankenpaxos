// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.simplegcbpaxos

/** @param vertexId
  *   TODO(mwhittaker): Figure out what other fields need to go here.
  */
@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Recover(
    vertexId: frankenpaxos.simplegcbpaxos.VertexId
    ) extends scalapb.GeneratedMessage with scalapb.Message[Recover] with scalapb.lenses.Updatable[Recover] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(vertexId.serializedSize) + vertexId.serializedSize
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
      _output__.writeUInt32NoTag(vertexId.serializedSize)
      vertexId.writeTo(_output__)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.simplegcbpaxos.Recover = {
      var __vertexId = this.vertexId
      var __requiredFields0: _root_.scala.Long = 0x1L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __vertexId = _root_.scalapb.LiteParser.readMessage(_input__, __vertexId)
            __requiredFields0 &= 0xfffffffffffffffeL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.simplegcbpaxos.Recover(
          vertexId = __vertexId
      )
    }
    def withVertexId(__v: frankenpaxos.simplegcbpaxos.VertexId): Recover = copy(vertexId = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => vertexId
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => vertexId.toPMessage
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.simplegcbpaxos.Recover
}

object Recover extends scalapb.GeneratedMessageCompanion[frankenpaxos.simplegcbpaxos.Recover] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.simplegcbpaxos.Recover] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.simplegcbpaxos.Recover = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.simplegcbpaxos.Recover(
      __fieldsMap(__fields.get(0)).asInstanceOf[frankenpaxos.simplegcbpaxos.VertexId]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.simplegcbpaxos.Recover] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.simplegcbpaxos.Recover(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[frankenpaxos.simplegcbpaxos.VertexId]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = SimpleGcBPaxosProto.javaDescriptor.getMessageTypes.get(20)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = SimpleGcBPaxosProto.scalaDescriptor.messages(20)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.simplegcbpaxos.VertexId
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.simplegcbpaxos.Recover(
    vertexId = frankenpaxos.simplegcbpaxos.VertexId.defaultInstance
  )
  implicit class RecoverLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplegcbpaxos.Recover]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.simplegcbpaxos.Recover](_l) {
    def vertexId: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.simplegcbpaxos.VertexId] = field(_.vertexId)((c_, f_) => c_.copy(vertexId = f_))
  }
  final val VERTEX_ID_FIELD_NUMBER = 1
}
