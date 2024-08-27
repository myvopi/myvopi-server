package com.example.myvopiserver.domain.command

data class EmailVerificationCommand(
    val id: Long,
    val userId: String,
    val email: String,
    val code: String,
    val chance: Int,
)

data class EmailVerifyReqCommand(
    val internalUserCommand: InternalUserCommand,
    val reqCode: String
)