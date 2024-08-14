package com.example.myvopiserver.interfaces.user

import com.commoncoremodule.enums.CountryCode

data class RegisterDto(
    val name: String,
    val userId: String, // TODO character limit
    val nationality: CountryCode,
    val password: String,
    val email: String, // TODO strong email verification needed
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