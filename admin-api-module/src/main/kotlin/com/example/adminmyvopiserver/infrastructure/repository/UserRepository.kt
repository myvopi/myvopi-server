package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByUuid(uuid: String): User?

    fun findByUserId(userId: String): User?

    fun findByUserIdAndUuid(userId: String, uuid: String): User?
}