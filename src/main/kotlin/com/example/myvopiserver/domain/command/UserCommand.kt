package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.CountryCode
import com.example.myvopiserver.common.enums.MemberRole

data class InternalUserCommand(
    val id: Long,
    val userId: String,
    val uuid: String,
    val password: String,
    val name: String,
    val nationality: CountryCode,
    val email: String,
    val role: MemberRole,
)

data class UserRegisterCommand(
    val name: String,
    val userId: String,
    val nationality: String,
    val password: String,
    val email: String,
)