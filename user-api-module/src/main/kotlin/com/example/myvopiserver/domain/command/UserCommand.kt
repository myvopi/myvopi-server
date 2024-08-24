package com.example.myvopiserver.domain.command

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.MemberRole
import com.commoncoremodule.enums.RoleStatus

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

data class InternalUserCommand(
    val id: Long,
    val userId: String,
    val uuid: String,
    val password: String,
    val name: String,
    val nationality: CountryCode,
    val email: String,
    val role: MemberRole,
    val status: RoleStatus,
)