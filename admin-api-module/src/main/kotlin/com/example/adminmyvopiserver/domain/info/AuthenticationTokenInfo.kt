package com.example.adminmyvopiserver.domain.info

data class AuthenticationTokenInfo(
    val accessToken: String,
    val refreshToken: String,
)
