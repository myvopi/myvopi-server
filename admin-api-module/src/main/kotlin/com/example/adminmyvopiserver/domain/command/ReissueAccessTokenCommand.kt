package com.example.adminmyvopiserver.domain.command

data class ReissueAccessTokenCommand(
    val refreshToken: String,
)