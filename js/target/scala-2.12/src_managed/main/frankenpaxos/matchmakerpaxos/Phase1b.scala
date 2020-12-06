// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.matchmakerpaxos

@SerialVersionUID(0L)
@scala.scalajs.js.annotation.JSExportAll
final case class Phase1b(
    round: _root_.scala.Int,
    acceptorIndex: _root_.scala.Int,
    vote: scala.Option[frankenpaxos.matchmakerpaxos.Phase1bVote] = None
    ) extends scalapb.GeneratedMessage with scalapb.Message[Phase1b] with scalapb.lenses.Updatable[Phase1b] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, round)
      __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(2, acceptorIndex)
      if (vote.isDefined) { __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(vote.get.serializedSize) + vote.get.serializedSize }
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
      _output__.writeInt32(1, round)
      _output__.writeInt32(2, acceptorIndex)
      vote.foreach { __v =>
        _output__.writeTag(3, 2)
        _output__.writeUInt32NoTag(__v.serializedSize)
        __v.writeTo(_output__)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): frankenpaxos.matchmakerpaxos.Phase1b = {
      var __round = this.round
      var __acceptorIndex = this.acceptorIndex
      var __vote = this.vote
      var __requiredFields0: _root_.scala.Long = 0x3L
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __round = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffeL
          case 16 =>
            __acceptorIndex = _input__.readInt32()
            __requiredFields0 &= 0xfffffffffffffffdL
          case 26 =>
            __vote = Option(_root_.scalapb.LiteParser.readMessage(_input__, __vote.getOrElse(frankenpaxos.matchmakerpaxos.Phase1bVote.defaultInstance)))
          case tag => _input__.skipField(tag)
        }
      }
      if (__requiredFields0 != 0L) { throw new _root_.com.google.protobuf.InvalidProtocolBufferException("Message missing required fields.") } 
      frankenpaxos.matchmakerpaxos.Phase1b(
          round = __round,
          acceptorIndex = __acceptorIndex,
          vote = __vote
      )
    }
    def withRound(__v: _root_.scala.Int): Phase1b = copy(round = __v)
    def withAcceptorIndex(__v: _root_.scala.Int): Phase1b = copy(acceptorIndex = __v)
    def getVote: frankenpaxos.matchmakerpaxos.Phase1bVote = vote.getOrElse(frankenpaxos.matchmakerpaxos.Phase1bVote.defaultInstance)
    def clearVote: Phase1b = copy(vote = None)
    def withVote(__v: frankenpaxos.matchmakerpaxos.Phase1bVote): Phase1b = copy(vote = Option(__v))
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => round
        case 2 => acceptorIndex
        case 3 => vote.orNull
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(round)
        case 2 => _root_.scalapb.descriptors.PInt(acceptorIndex)
        case 3 => vote.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = frankenpaxos.matchmakerpaxos.Phase1b
}

object Phase1b extends scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakerpaxos.Phase1b] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[frankenpaxos.matchmakerpaxos.Phase1b] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): frankenpaxos.matchmakerpaxos.Phase1b = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    frankenpaxos.matchmakerpaxos.Phase1b(
      __fieldsMap(__fields.get(0)).asInstanceOf[_root_.scala.Int],
      __fieldsMap(__fields.get(1)).asInstanceOf[_root_.scala.Int],
      __fieldsMap.get(__fields.get(2)).asInstanceOf[scala.Option[frankenpaxos.matchmakerpaxos.Phase1bVote]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[frankenpaxos.matchmakerpaxos.Phase1b] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      frankenpaxos.matchmakerpaxos.Phase1b(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).get.as[_root_.scala.Int],
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).flatMap(_.as[scala.Option[frankenpaxos.matchmakerpaxos.Phase1bVote]])
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = MatchmakerPaxosProto.javaDescriptor.getMessageTypes.get(6)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = MatchmakerPaxosProto.scalaDescriptor.messages(6)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 3 => __out = frankenpaxos.matchmakerpaxos.Phase1bVote
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = frankenpaxos.matchmakerpaxos.Phase1b(
    round = 0,
    acceptorIndex = 0
  )
  implicit class Phase1bLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakerpaxos.Phase1b]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, frankenpaxos.matchmakerpaxos.Phase1b](_l) {
    def round: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.round)((c_, f_) => c_.copy(round = f_))
    def acceptorIndex: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.acceptorIndex)((c_, f_) => c_.copy(acceptorIndex = f_))
    def vote: _root_.scalapb.lenses.Lens[UpperPB, frankenpaxos.matchmakerpaxos.Phase1bVote] = field(_.getVote)((c_, f_) => c_.copy(vote = Option(f_)))
    def optionalVote: _root_.scalapb.lenses.Lens[UpperPB, scala.Option[frankenpaxos.matchmakerpaxos.Phase1bVote]] = field(_.vote)((c_, f_) => c_.copy(vote = f_))
  }
  final val ROUND_FIELD_NUMBER = 1
  final val ACCEPTOR_INDEX_FIELD_NUMBER = 2
  final val VOTE_FIELD_NUMBER = 3
}
