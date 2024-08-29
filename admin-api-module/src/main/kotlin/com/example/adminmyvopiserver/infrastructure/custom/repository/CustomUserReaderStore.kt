package com.example.adminmyvopiserver.infrastructure.custom.repository

import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import org.springframework.stereotype.Repository

@Repository
interface CustomUserReaderStore {

    fun searchUsersRequest(command: UserAdminSearchCommand): List<User>
}