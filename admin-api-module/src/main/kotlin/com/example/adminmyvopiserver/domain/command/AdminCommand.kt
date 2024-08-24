package com.example.adminmyvopiserver.domain.command

data class AdminLoginCommand(
    val userId: String,
    val password: String,
)