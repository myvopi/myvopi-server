package com.example.myvopiserver.domain.command

data class ReplySearchCommand(
    val internalUserCommand: InternalUserCommand?,
    val commentUuid: String,
    val reqPage: Int,
)