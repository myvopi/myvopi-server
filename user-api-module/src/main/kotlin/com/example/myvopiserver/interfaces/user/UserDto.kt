package com.example.myvopiserver.interfaces.user

import com.commoncoremodule.enums.CountryCode

data class RegisterDto(
    val name: String,
    val nationality: CountryCode,
    val password: String,
    val email: String,
)

data class LoginDto(
    val email: String,
    val password: String,
)

data class EmailVerificationDto(
    val code: String,
)

data class ReissueAccessTokenDto(
    val refreshToken: String,
)