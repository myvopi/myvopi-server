package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.User


interface UserReaderStore {

    fun saveUser(user: User): User

    fun findUserByUuid(uuid: String): User?

    fun findUserByUserId(userId: String): User?

    fun userExistsByUserId(userId: String): Boolean

    fun userExistsByEmail(email: String): Boolean
}