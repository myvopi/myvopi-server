package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.role.User

interface UserReaderStore {

    fun saveUser(user: User): User

    /**
    * validation usages
    * */
    fun findUserByUserId(userId: String): User?

    fun findUserByEmail(email: String): User?
}