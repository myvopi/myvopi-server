package com.entitycoremodule.command

import com.commoncoremodule.enums.CountryCode

data class UserRegisterCommand(
    val name: String,
    val userId: String,
    val nationality: CountryCode,
    val password: String,
    val email: String,
)

data class UserLoginCommand(
    val userId: String,
    val password: String,
)