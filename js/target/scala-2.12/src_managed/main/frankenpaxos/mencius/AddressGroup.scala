// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.mencius

@SerialVersionUID(0L)
final case class AddressGroup(
    address: _root_.scala.collection.Seq[frankenpaxos.mencius.HostPortProto] = _root_.scala.collection.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[AddressGroup] with scalapb.lenses.Updatable[AddressGroup] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      address.foreach(address => __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(address.serializedSize) + address.serializedSize)
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
      address.foreach { __v =>
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.mencius.AddressGroup = {
      val __address = (_root_.scala.collection.immutable.Vector.newBuilder[frankenpaxos.mencius.HostPortProto] ++= this.address)
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __address += _root_.scalapb.LiteParser.readMessage(_input__, frankenpaxos.mencius.HostPortProto.defaultInstance)
          case tag => _input__.skipField(tag)
        }
      }
      frankenpaxos.mencius.AddressGroup(
          address = __address.result()
      )
    }
    def clearAddress = copy(address = _root_.scala.collection.Seq.empty)
    def addAddress(__vs: frankenpaxos.mencius.HostPortProto*): AddressGroup = addAllAddress(__vs)
    def addAllAddress(__vs: TraversableOnce[frankenpaxos.mencius.HostPortProto]): AddressGroup = copy(address = address ++ __vs)
    def withAddress(__v: _root_.scala.collection.Seq[frankenpaxos.mencius.HostPortProto]): AddressGroup = copy(address = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => address
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PRepeated(address.map(_.toPMessage)(_root_.scala.collection.breakOut))
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.mencius.AddressGroup
}

object AddressGroup extends scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.AddressGroup] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.mencius.AddressGroup] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.mencius.AddressGroup = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.mencius.AddressGroup(
      __fieldsMap.getOrElse(__fields.get(0), Nil).asInstanceOf[_root_.scala.collection.Seq[frankenpaxos.mencius.HostPortProto]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.mencius.AddressGroup] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.mencius.AddressGroup(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.collection.Seq[frankenpaxos.mencius.HostPortProto]]).getOrElse(_root_.scala.collection.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ConfigProto.javaDescriptor.getMessageTypes.get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ConfigProto.scalaDescriptor.messages(1)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = frankenpaxos.mencius.HostPortProto
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.mencius.AddressGroup(
  )
  implicit class AddressGroupLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.mencius.AddressGroup]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.mencius.AddressGroup](_l) {
    def address: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.collection.Seq[frankenpaxos.mencius.HostPortProto]] = field(_.address)((c_, f_) => c_.copy(address = f_))
  }
  final val ADDRESS_FIELD_NUMBER = 1
}
