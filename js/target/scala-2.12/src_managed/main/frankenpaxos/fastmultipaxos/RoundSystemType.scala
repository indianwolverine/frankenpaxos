// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.fastmultipaxos

sealed trait RoundSystemType extends _root_.scalapb.GeneratedEnum {
  type EnumType = RoundSystemType
  def isClassicRoundRobin: _root_.scala.Boolean = false
  def isRoundZeroFast: _root_.scala.Boolean = false
  def isMixedRoundRobin: _root_.scala.Boolean = false
  def companion: _root_.scalapb.GeneratedEnumCompanion[RoundSystemType] = frankenpaxos.fastmultipaxos.RoundSystemType
}

object RoundSystemType extends _root_.scalapb.GeneratedEnumCompanion[RoundSystemType] {
  implicit def enumCompanion: _root_.scalapb.GeneratedEnumCompanion[RoundSystemType] = this
  @SerialVersionUID(0L)
  case object CLASSIC_ROUND_ROBIN extends RoundSystemType {
    val value = 0
    val index = 0
    val name = "CLASSIC_ROUND_ROBIN"
    override def isClassicRoundRobin: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object ROUND_ZERO_FAST extends RoundSystemType {
    val value = 1
    val index = 1
    val name = "ROUND_ZERO_FAST"
    override def isRoundZeroFast: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  case object MIXED_ROUND_ROBIN extends RoundSystemType {
    val value = 2
    val index = 2
    val name = "MIXED_ROUND_ROBIN"
    override def isMixedRoundRobin: _root_.scala.Boolean = true
  }
  
  @SerialVersionUID(0L)
  final case class Unrecognized(value: _root_.scala.Int) extends RoundSystemType with _root_.scalapb.UnrecognizedEnum
  
  lazy val values = scala.collection.Seq(CLASSIC_ROUND_ROBIN, ROUND_ZERO_FAST, MIXED_ROUND_ROBIN)
  def fromValue(value: _root_.scala.Int): RoundSystemType = value match {
    case 0 => CLASSIC_ROUND_ROBIN
    case 1 => ROUND_ZERO_FAST
    case 2 => MIXED_ROUND_ROBIN
    case __other => Unrecognized(__other)
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.EnumDescriptor = ConfigProtoCompanion.javaDescriptor.getEnumTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.EnumDescriptor = ConfigProtoCompanion.scalaDescriptor.enums(0)
}