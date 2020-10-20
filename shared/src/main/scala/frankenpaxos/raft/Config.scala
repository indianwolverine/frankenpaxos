package frankenpaxos.raft

case class Config[Transport <: frankenpaxos.Transport[Transport]](
    participantAddresses: Seq[Transport#Address],
    clientAddresses:      Seq[Transport#Address]
) {}
