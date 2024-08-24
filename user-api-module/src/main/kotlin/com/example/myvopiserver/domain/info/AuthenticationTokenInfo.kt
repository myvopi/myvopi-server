package com.example.myvopiserver.domain.info

data class AuthenticationTokenInfo(
    val accessToken: String,
    val refreshToken: String,
)
