package com.example.adminmyvopiserver.domain.interfaces

import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.User

interface UserReaderStore {

    fun searchUsersRequest(command: UserAdminSearchCommand): List<User>

    fun findUserByUuid(uuid: String): User?

    fun findUserByUserId(userId: String): User?

    fun findUserByUserIdAndUuid(userId: String, uuid: String): User?

    fun saveUser(user: User): User
}