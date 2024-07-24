package com.example.myvopiserver.domain.command

data class EmailVerificationCommand(
    val id: Long,
    val userId: String,
    val email: String,
    val code: String
)

data class EmailVerifyReqCommand(
    val internalUserCommand: InternalUserCommand,
    val reqCode: String
)