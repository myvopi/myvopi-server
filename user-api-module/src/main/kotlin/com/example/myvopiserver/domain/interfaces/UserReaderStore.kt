package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.User

interface UserReaderStore {

    fun saveUser(user: User): User

    fun findUserByUuid(uuid: String): User?

    fun findUserByUserId(userId: String): User?

    fun userExistsByUserIdOrEmail(userId: String, email: String): Boolean
}