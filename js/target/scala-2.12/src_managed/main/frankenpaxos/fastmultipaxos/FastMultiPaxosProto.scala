// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO2

package frankenpaxos.fastmultipaxos

object FastMultiPaxosProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
    scalapb.options.ScalapbProto
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq(
    frankenpaxos.fastmultipaxos.Noop,
    frankenpaxos.fastmultipaxos.AnyVal,
    frankenpaxos.fastmultipaxos.AnyValSuffix,
    frankenpaxos.fastmultipaxos.Command,
    frankenpaxos.fastmultipaxos.ProposeRequest,
    frankenpaxos.fastmultipaxos.ProposeReply,
    frankenpaxos.fastmultipaxos.LeaderInfo,
    frankenpaxos.fastmultipaxos.Phase1a,
    frankenpaxos.fastmultipaxos.Phase1bVote,
    frankenpaxos.fastmultipaxos.Phase1b,
    frankenpaxos.fastmultipaxos.Phase1bNack,
    frankenpaxos.fastmultipaxos.Phase2a,
    frankenpaxos.fastmultipaxos.Phase2aBuffer,
    frankenpaxos.fastmultipaxos.Phase2b,
    frankenpaxos.fastmultipaxos.Phase2bBuffer,
    frankenpaxos.fastmultipaxos.ValueChosen,
    frankenpaxos.fastmultipaxos.ValueChosenBuffer,
    frankenpaxos.fastmultipaxos.ClientInbound,
    frankenpaxos.fastmultipaxos.LeaderInbound,
    frankenpaxos.fastmultipaxos.AcceptorInbound
  )
  private lazy val ProtoBytes: Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.Seq(
  """CjBmcmFua2VucGF4b3MvZmFzdG11bHRpcGF4b3MvRmFzdE11bHRpUGF4b3MucHJvdG8SG2ZyYW5rZW5wYXhvcy5mYXN0bXVsd
  GlwYXhvcxoVc2NhbGFwYi9zY2FsYXBiLnByb3RvIgYKBE5vb3AiCAoGQW55VmFsIg4KDEFueVZhbFN1ZmZpeCKSAQoHQ29tbWFuZ
  BIlCg5jbGllbnRfYWRkcmVzcxgBIAIoDFINY2xpZW50QWRkcmVzcxIpChBjbGllbnRfcHNldWRvbnltGAIgAigFUg9jbGllbnRQc
  2V1ZG9ueW0SGwoJY2xpZW50X2lkGAMgAigFUghjbGllbnRJZBIYCgdjb21tYW5kGAQgAigMUgdjb21tYW5kImYKDlByb3Bvc2VSZ
  XF1ZXN0EhQKBXJvdW5kGAEgAigFUgVyb3VuZBI+Cgdjb21tYW5kGAIgAigLMiQuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zL
  kNvbW1hbmRSB2NvbW1hbmQihAEKDFByb3Bvc2VSZXBseRIUCgVyb3VuZBgBIAIoBVIFcm91bmQSKQoQY2xpZW50X3BzZXVkb255b
  RgCIAIoBVIPY2xpZW50UHNldWRvbnltEhsKCWNsaWVudF9pZBgDIAIoBVIIY2xpZW50SWQSFgoGcmVzdWx0GAQgAigMUgZyZXN1b
  HQiIgoKTGVhZGVySW5mbxIUCgVyb3VuZBgBIAIoBVIFcm91bmQiaQoHUGhhc2UxYRIUCgVyb3VuZBgBIAIoBVIFcm91bmQSKAoPY
  2hvc2VuV2F0ZXJtYXJrGAIgAigFUg9jaG9zZW5XYXRlcm1hcmsSHgoKY2hvc2VuU2xvdBgDIAMoBVIKY2hvc2VuU2xvdCLDAQoLU
  Ghhc2UxYlZvdGUSEgoEc2xvdBgBIAIoBVIEc2xvdBIcCgl2b3RlUm91bmQYAiACKAVSCXZvdGVSb3VuZBJACgdjb21tYW5kGAMgA
  SgLMiQuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLkNvbW1hbmRIAFIHY29tbWFuZBI3CgRub29wGAQgASgLMiEuZnJhbmtlb
  nBheG9zLmZhc3RtdWx0aXBheG9zLk5vb3BIAFIEbm9vcEIHCgV2YWx1ZSJ+CgdQaGFzZTFiEh8KC2FjY2VwdG9yX2lkGAEgAigFU
  gphY2NlcHRvcklkEhQKBXJvdW5kGAIgAigFUgVyb3VuZBI8CgR2b3RlGAMgAygLMiguZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBhe
  G9zLlBoYXNlMWJWb3RlUgR2b3RlIkQKC1BoYXNlMWJOYWNrEh8KC2FjY2VwdG9yX2lkGAEgAigFUgphY2NlcHRvcklkEhQKBXJvd
  W5kGAIgAigFUgVyb3VuZCK8AgoHUGhhc2UyYRISCgRzbG90GAEgAigFUgRzbG90EhQKBXJvdW5kGAIgAigFUgVyb3VuZBJACgdjb
  21tYW5kGAMgASgLMiQuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLkNvbW1hbmRIAFIHY29tbWFuZBI3CgRub29wGAQgASgLM
  iEuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLk5vb3BIAFIEbm9vcBI3CgNhbnkYBSABKAsyIy5mcmFua2VucGF4b3MuZmFzd
  G11bHRpcGF4b3MuQW55VmFsSABSA2FueRJKCgphbnlfc3VmZml4GAYgASgLMikuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zL
  kFueVZhbFN1ZmZpeEgAUglhbnlTdWZmaXhCBwoFdmFsdWUiTwoNUGhhc2UyYUJ1ZmZlchI+CgdwaGFzZTJhGAEgAygLMiQuZnJhb
  mtlbnBheG9zLmZhc3RtdWx0aXBheG9zLlBoYXNlMmFSB3BoYXNlMmEi1wEKB1BoYXNlMmISHwoLYWNjZXB0b3JfaWQYASACKAVSC
  mFjY2VwdG9ySWQSEgoEc2xvdBgCIAIoBVIEc2xvdBIUCgVyb3VuZBgDIAIoBVIFcm91bmQSQAoHY29tbWFuZBgEIAEoCzIkLmZyY
  W5rZW5wYXhvcy5mYXN0bXVsdGlwYXhvcy5Db21tYW5kSABSB2NvbW1hbmQSNwoEbm9vcBgFIAEoCzIhLmZyYW5rZW5wYXhvcy5mY
  XN0bXVsdGlwYXhvcy5Ob29wSABSBG5vb3BCBgoEdm90ZSJPCg1QaGFzZTJiQnVmZmVyEj4KB3BoYXNlMmIYASADKAsyJC5mcmFua
  2VucGF4b3MuZmFzdG11bHRpcGF4b3MuUGhhc2UyYlIHcGhhc2UyYiKlAQoLVmFsdWVDaG9zZW4SEgoEc2xvdBgBIAIoBVIEc2xvd
  BJACgdjb21tYW5kGAIgASgLMiQuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLkNvbW1hbmRIAFIHY29tbWFuZBI3CgRub29wG
  AMgASgLMiEuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLk5vb3BIAFIEbm9vcEIHCgV2YWx1ZSJgChFWYWx1ZUNob3NlbkJ1Z
  mZlchJLCgx2YWx1ZV9jaG9zZW4YASADKAsyKC5mcmFua2VucGF4b3MuZmFzdG11bHRpcGF4b3MuVmFsdWVDaG9zZW5SC3ZhbHVlQ
  2hvc2VuIrgBCg1DbGllbnRJbmJvdW5kEkoKC2xlYWRlcl9pbmZvGAEgASgLMicuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zL
  kxlYWRlckluZm9IAFIKbGVhZGVySW5mbxJQCg1wcm9wb3NlX3JlcGx5GAIgASgLMikuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBhe
  G9zLlByb3Bvc2VSZXBseUgAUgxwcm9wb3NlUmVwbHlCCQoHcmVxdWVzdCLLBAoNTGVhZGVySW5ib3VuZBJWCg9wcm9wb3NlX3Jlc
  XVlc3QYASABKAsyKy5mcmFua2VucGF4b3MuZmFzdG11bHRpcGF4b3MuUHJvcG9zZVJlcXVlc3RIAFIOcHJvcG9zZVJlcXVlc3QSQ
  AoHcGhhc2UxYhgCIAEoCzIkLmZyYW5rZW5wYXhvcy5mYXN0bXVsdGlwYXhvcy5QaGFzZTFiSABSB3BoYXNlMWISTQoMcGhhc2UxY
  l9uYWNrGAMgASgLMiguZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLlBoYXNlMWJOYWNrSABSC3BoYXNlMWJOYWNrEkAKB3BoY
  XNlMmIYBCABKAsyJC5mcmFua2VucGF4b3MuZmFzdG11bHRpcGF4b3MuUGhhc2UyYkgAUgdwaGFzZTJiElMKDnBoYXNlMmJfYnVmZ
  mVyGAUgASgLMiouZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLlBoYXNlMmJCdWZmZXJIAFINcGhhc2UyYkJ1ZmZlchJNCgx2Y
  Wx1ZV9jaG9zZW4YBiABKAsyKC5mcmFua2VucGF4b3MuZmFzdG11bHRpcGF4b3MuVmFsdWVDaG9zZW5IAFILdmFsdWVDaG9zZW4SY
  AoTdmFsdWVfY2hvc2VuX2J1ZmZlchgHIAEoCzIuLmZyYW5rZW5wYXhvcy5mYXN0bXVsdGlwYXhvcy5WYWx1ZUNob3NlbkJ1ZmZlc
  kgAUhF2YWx1ZUNob3NlbkJ1ZmZlckIJCgdyZXF1ZXN0Is0CCg9BY2NlcHRvckluYm91bmQSVgoPcHJvcG9zZV9yZXF1ZXN0GAEgA
  SgLMisuZnJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLlByb3Bvc2VSZXF1ZXN0SABSDnByb3Bvc2VSZXF1ZXN0EkAKB3BoYXNlM
  WEYAiABKAsyJC5mcmFua2VucGF4b3MuZmFzdG11bHRpcGF4b3MuUGhhc2UxYUgAUgdwaGFzZTFhEkAKB3BoYXNlMmEYAyABKAsyJ
  C5mcmFua2VucGF4b3MuZmFzdG11bHRpcGF4b3MuUGhhc2UyYUgAUgdwaGFzZTJhElMKDnBoYXNlMmFfYnVmZmVyGAQgASgLMiouZ
  nJhbmtlbnBheG9zLmZhc3RtdWx0aXBheG9zLlBoYXNlMmFCdWZmZXJIAFINcGhhc2UyYUJ1ZmZlckIJCgdyZXF1ZXN0QiLiPx8KG
  2ZyYW5rZW5wYXhvcy5mYXN0bXVsdGlwYXhvcxAB"""
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