package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByUuid(uuid: String): User?

    fun findByEmail(email: String): User?

    fun existsByUserIdOrEmail(userId: String, email: String): Boolean

    fun existsByEmail(email: String): Boolean
}