// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.statemachine

@SerialVersionUID(0L)
final case class SetKeyValuePair(
    key: _root_.scala.Predef.String,
    value: _root_.scala.Predef.String
    ) extends scalapb.GeneratedMessage with scalapb.Message[SetKeyValuePair] with scalapb.lenses.Updatable[SetKeyValuePair] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, key)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, value)
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
      _output__.writeString(1, key)
      _output__.writeString(2, value)
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.statemachine.SetKeyValuePair = {
      var __key = this.key
      var __value = this.value
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __key = _input__.readString()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 18 =>
            __value = _input__.readString()
            __requiredFields0 &= 0xfffffffffffffffdL
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.statemachine.SetKeyValuePair(
          key = __key,
          value = __value
      )
    }
    def withKey(__v: _root_.scala.Predef.String): SetKeyValuePair = copy(key = __v)
    def withValue(__v: _root_.scala.Predef.String): SetKeyValuePair = copy(value = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => key
        case 2 => value
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PString(key)
        case 2 => _root_.scalapb.descriptors.PString(value)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.statemachine.SetKeyValuePair
}

object SetKeyValuePair extends scalapb.GeneratedMessageCompanion[frankenpaxos.statemachine.SetKeyValuePair] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.statemachine.SetKeyValuePair] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.statemachine.SetKeyValuePair = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.statemachine.SetKeyValuePair(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Predef.String],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Predef.String]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.statemachine.SetKeyValuePair] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.statemachine.SetKeyValuePair(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Predef.String],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Predef.String]
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = KeyValueStoreProtoCompanion.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = KeyValueStoreProtoCompanion.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.statemachine.SetKeyValuePair(
    key = "",
    value = ""
  )
  implicit class SetKeyValuePairLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.statemachine.SetKeyValuePair]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.statemachine.SetKeyValuePair](_l) {
    def key: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.key)((c_, f_) => c_.copy(key = f_))
    def value: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.value)((c_, f_) => c_.copy(value = f_))
  }
  final val KEY_FIELD_NUMBER = 1
  final val VALUE_FIELD_NUMBER = 2
}
