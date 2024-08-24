package com.example.adminmyvopiserver.domain.command

import com.commoncoremodule.enums.CountryCode
import com.commoncoremodule.enums.RoleStatus

data class UserAdminSearchCommand(
    val userId: String?,
    val userUuid: String?,
    val userName: String?,
    val nationality: CountryCode?,
    val email: String?,
    val status: RoleStatus?,
    val reqPage: Int,
)

data class CommentAdminSearchCommand(
    val userId: Long,
    val userUuid: String,
)