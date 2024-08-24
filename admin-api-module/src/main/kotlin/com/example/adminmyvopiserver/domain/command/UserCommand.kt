package com.example.adminmyvopiserver.domain.command

import com.commoncoremodule.enums.RoleStatus

data class UserAdminSetRoleStatusCommand(
    val userId: String,
    val userUuid: String,
    val email: String,
    val status: RoleStatus,
)