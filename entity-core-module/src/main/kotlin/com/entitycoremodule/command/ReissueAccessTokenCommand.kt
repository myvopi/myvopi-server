package com.entitycoremodule.command

data class ReissueAccessTokenCommand(
    val refreshToken: String,
)