package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.role.EmailVerification
import com.example.myvopiserver.domain.role.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailVerificationRepository: JpaRepository<EmailVerification, Long> {

    fun findByUser(user: User): EmailVerification?
}