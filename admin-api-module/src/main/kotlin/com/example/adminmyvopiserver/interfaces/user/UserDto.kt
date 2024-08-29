package com.example.adminmyvopiserver.interfaces.user

data class UserAdminSetRoleStatusDto(
    val userId: String,
    val userUuid: String,
    val email: String,
)

data class ContentsByUserDto(
    val userId: String,
    val userUuid: String,
    val email: String,
    val reqPage: Int,
)