// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.batchedunreplicated

object BatchedUnreplicatedProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    scalapb.options.ScalapbProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq(
    frankenpaxos.batchedunreplicated.Command,
    frankenpaxos.batchedunreplicated.Result,
    frankenpaxos.batchedunreplicated.ClientRequest,
    frankenpaxos.batchedunreplicated.ClientRequestBatch,
    frankenpaxos.batchedunreplicated.ClientReplyBatch,
    frankenpaxos.batchedunreplicated.ClientReply,
    frankenpaxos.batchedunreplicated.ClientInbound,
    frankenpaxos.batchedunreplicated.BatcherInbound,
    frankenpaxos.batchedunreplicated.ServerInbound,
    frankenpaxos.batchedunreplicated.ProxyServerInbound
  )
  private lazy val ProtoBytes: Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.Seq(
  """CjpmcmFua2VucGF4b3MvYmF0Y2hlZHVucmVwbGljYXRlZC9CYXRjaGVkVW5yZXBsaWNhdGVkLnByb3RvEiBmcmFua2VucGF4b
  3MuYmF0Y2hlZHVucmVwbGljYXRlZBoVc2NhbGFwYi9zY2FsYXBiLnByb3RvIpgBCgdDb21tYW5kEiUKDmNsaWVudF9hZGRyZXNzG
  AEgAigMUg1jbGllbnRBZGRyZXNzEh0KCmNvbW1hbmRfaWQYAiACKAVSCWNvbW1hbmRJZBIYCgdjb21tYW5kGAMgAigMUgdjb21tY
  W5kOi3iPyoaKEBzY2FsYS5zY2FsYWpzLmpzLmFubm90YXRpb24uSlNFeHBvcnRBbGwilQEKBlJlc3VsdBIlCg5jbGllbnRfYWRkc
  mVzcxgBIAIoDFINY2xpZW50QWRkcmVzcxIdCgpjb21tYW5kX2lkGAIgAigFUgljb21tYW5kSWQSFgoGcmVzdWx0GAMgAigMUgZyZ
  XN1bHQ6LeI/KhooQHNjYWxhLnNjYWxhanMuanMuYW5ub3RhdGlvbi5KU0V4cG9ydEFsbCKDAQoNQ2xpZW50UmVxdWVzdBJDCgdjb
  21tYW5kGAEgAigLMikuZnJhbmtlbnBheG9zLmJhdGNoZWR1bnJlcGxpY2F0ZWQuQ29tbWFuZFIHY29tbWFuZDot4j8qGihAc2Nhb
  GEuc2NhbGFqcy5qcy5hbm5vdGF0aW9uLkpTRXhwb3J0QWxsIogBChJDbGllbnRSZXF1ZXN0QmF0Y2gSQwoHY29tbWFuZBgBIAMoC
  zIpLmZyYW5rZW5wYXhvcy5iYXRjaGVkdW5yZXBsaWNhdGVkLkNvbW1hbmRSB2NvbW1hbmQ6LeI/KhooQHNjYWxhLnNjYWxhanMua
  nMuYW5ub3RhdGlvbi5KU0V4cG9ydEFsbCKDAQoQQ2xpZW50UmVwbHlCYXRjaBJACgZyZXN1bHQYASADKAsyKC5mcmFua2VucGF4b
  3MuYmF0Y2hlZHVucmVwbGljYXRlZC5SZXN1bHRSBnJlc3VsdDot4j8qGihAc2NhbGEuc2NhbGFqcy5qcy5hbm5vdGF0aW9uLkpTR
  Xhwb3J0QWxsIn4KC0NsaWVudFJlcGx5EkAKBnJlc3VsdBgBIAIoCzIoLmZyYW5rZW5wYXhvcy5iYXRjaGVkdW5yZXBsaWNhdGVkL
  lJlc3VsdFIGcmVzdWx0Oi3iPyoaKEBzY2FsYS5zY2FsYWpzLmpzLmFubm90YXRpb24uSlNFeHBvcnRBbGwinQEKDUNsaWVudEluY
  m91bmQSUgoMY2xpZW50X3JlcGx5GAEgASgLMi0uZnJhbmtlbnBheG9zLmJhdGNoZWR1bnJlcGxpY2F0ZWQuQ2xpZW50UmVwbHlIA
  FILY2xpZW50UmVwbHk6LeI/KhooQHNjYWxhLnNjYWxhanMuanMuYW5ub3RhdGlvbi5KU0V4cG9ydEFsbEIJCgdyZXF1ZXN0IqQBC
  g5CYXRjaGVySW5ib3VuZBJYCg5jbGllbnRfcmVxdWVzdBgBIAEoCzIvLmZyYW5rZW5wYXhvcy5iYXRjaGVkdW5yZXBsaWNhdGVkL
  kNsaWVudFJlcXVlc3RIAFINY2xpZW50UmVxdWVzdDot4j8qGihAc2NhbGEuc2NhbGFqcy5qcy5hbm5vdGF0aW9uLkpTRXhwb3J0Q
  WxsQgkKB3JlcXVlc3QiswEKDVNlcnZlckluYm91bmQSaAoUY2xpZW50X3JlcXVlc3RfYmF0Y2gYASABKAsyNC5mcmFua2VucGF4b
  3MuYmF0Y2hlZHVucmVwbGljYXRlZC5DbGllbnRSZXF1ZXN0QmF0Y2hIAFISY2xpZW50UmVxdWVzdEJhdGNoOi3iPyoaKEBzY2FsY
  S5zY2FsYWpzLmpzLmFubm90YXRpb24uSlNFeHBvcnRBbGxCCQoHcmVxdWVzdCKyAQoSUHJveHlTZXJ2ZXJJbmJvdW5kEmIKEmNsa
  WVudF9yZXBseV9iYXRjaBgBIAEoCzIyLmZyYW5rZW5wYXhvcy5iYXRjaGVkdW5yZXBsaWNhdGVkLkNsaWVudFJlcGx5QmF0Y2hIA
  FIQY2xpZW50UmVwbHlCYXRjaDot4j8qGihAc2NhbGEuc2NhbGFqcy5qcy5hbm5vdGF0aW9uLkpTRXhwb3J0QWxsQgkKB3JlcXVlc
  3RCJ+I/JAogZnJhbmtlbnBheG9zLmJhdGNoZWR1bnJlcGxpY2F0ZWQQAQ=="""
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