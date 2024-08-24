package com.example.adminmyvopiserver.interfaces.user

import com.commoncoremodule.enums.RoleStatus

data class UserAdminSetRoleStatusDto(
    val userId: String,
    val userUuid: String,
    val email: String,
    val status: RoleStatus,
)