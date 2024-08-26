package com.example.adminmyvopiserver.domain.command

import org.springframework.data.domain.Pageable

data class UserAdminSetRoleStatusCommand(
    val userId: String,
    val userUuid: String,
    val email: String,
)

data class ContentsByUserCommand(
    val userId: String,
    val userUuid: String,
    val email: String,
    val pageable: Pageable,
)