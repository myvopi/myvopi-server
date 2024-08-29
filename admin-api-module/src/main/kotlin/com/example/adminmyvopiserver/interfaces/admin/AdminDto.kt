package com.example.adminmyvopiserver.interfaces.admin

data class LoginDto(
    val userId: String,
    val password: String,
)

data class ReissueAccessTokenDto(
    val refreshToken: String,
)