package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.role.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByUuid(uuid: String): User?

    fun findByUserId(userId: String): User?

    fun existsUserByUuid(uuid: String): Boolean

    fun existsUserByEmail(email: String): Boolean
}