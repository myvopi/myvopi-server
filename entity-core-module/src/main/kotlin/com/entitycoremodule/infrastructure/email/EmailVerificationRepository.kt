package com.entitycoremodule.infrastructure.email

import com.entitycoremodule.domain.email.EmailVerification
import com.entitycoremodule.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailVerificationRepository: JpaRepository<EmailVerification, Long> {

    fun findByUser(user: User): EmailVerification?
}