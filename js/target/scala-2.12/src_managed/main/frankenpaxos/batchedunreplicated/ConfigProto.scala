// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.batchedunreplicated

object ConfigProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    scalapb.options.ScalapbProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq(
    frankenpaxos.batchedunreplicated.HostPortProto,
    frankenpaxos.batchedunreplicated.NettyConfigProto
  )
  private lazy val ProtoBytes: Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.Seq(
  """Ci1mcmFua2VucGF4b3MvYmF0Y2hlZHVucmVwbGljYXRlZC9Db25maWcucHJvdG8SIGZyYW5rZW5wYXhvcy5iYXRjaGVkdW5yZ
  XBsaWNhdGVkGhVzY2FsYXBiL3NjYWxhcGIucHJvdG8iNwoNSG9zdFBvcnRQcm90bxISCgRob3N0GAEgAigJUgRob3N0EhIKBHBvc
  nQYAiACKAVSBHBvcnQipwIKEE5ldHR5Q29uZmlnUHJvdG8SWAoPYmF0Y2hlcl9hZGRyZXNzGAEgAygLMi8uZnJhbmtlbnBheG9zL
  mJhdGNoZWR1bnJlcGxpY2F0ZWQuSG9zdFBvcnRQcm90b1IOYmF0Y2hlckFkZHJlc3MSVgoOc2VydmVyX2FkZHJlc3MYAiACKAsyL
  y5mcmFua2VucGF4b3MuYmF0Y2hlZHVucmVwbGljYXRlZC5Ib3N0UG9ydFByb3RvUg1zZXJ2ZXJBZGRyZXNzEmEKFHByb3h5X3Nlc
  nZlcl9hZGRyZXNzGAMgAygLMi8uZnJhbmtlbnBheG9zLmJhdGNoZWR1bnJlcGxpY2F0ZWQuSG9zdFBvcnRQcm90b1IScHJveHlTZ
  XJ2ZXJBZGRyZXNzQifiPyQKIGZyYW5rZW5wYXhvcy5iYXRjaGVkdW5yZXBsaWNhdGVkEAE="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, Array(
      scalapb.options.ScalapbProto.javaDescriptor
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}