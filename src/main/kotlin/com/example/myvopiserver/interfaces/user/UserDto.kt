package com.example.myvopiserver.interfaces.user

import com.example.myvopiserver.common.enums.CountryCode

data class RegisterDto(
    val name: String,
    val userId: String,
    val nationality: CountryCode,
    val password: String,
    val email: String,
)

data class LoginDto(
    val userId: String,
    val password: String,
)

data class EmailVerificationDto(
    val code: String,
)

data class ReissueAccessTokenDto(
    val refreshToken: String,
)