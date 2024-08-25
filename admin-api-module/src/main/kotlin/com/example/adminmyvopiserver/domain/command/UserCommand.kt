package com.example.adminmyvopiserver.domain.command

data class UserAdminSetRoleStatusCommand(
    val userId: String,
    val userUuid: String,
    val email: String,
)