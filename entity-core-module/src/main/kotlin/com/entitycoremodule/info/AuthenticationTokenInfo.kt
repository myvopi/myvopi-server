package com.entitycoremodule.info

data class AuthenticationTokenInfo(
    val accessToken: String,
    val refreshToken: String,
)
