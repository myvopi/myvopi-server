package com.example.adminmyvopiserver.infrastructure.repository

import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.EmailVerification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailVerificationRepository: JpaRepository<EmailVerification, Long> {

    fun findByUser(user: User): EmailVerification?
}