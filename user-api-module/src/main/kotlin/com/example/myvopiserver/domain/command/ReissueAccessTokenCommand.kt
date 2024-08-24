package com.example.myvopiserver.domain.command

data class ReissueAccessTokenCommand(
    val refreshToken: String,
)