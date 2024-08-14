package com.entitycoremodule.command

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.MemberRole

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
