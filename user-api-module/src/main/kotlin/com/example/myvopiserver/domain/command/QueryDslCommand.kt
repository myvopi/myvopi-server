package com.example.myvopiserver.domain.command

data class UpdateClauseCommand(
    val pathName: String,
    val value: Any,
)