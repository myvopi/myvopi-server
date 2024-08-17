package com.entitycoremodule.infrastructure.users

import com.entitycoremodule.domain.email.EmailVerification
import com.entitycoremodule.domain.interfaces.users.EmailVerificationReaderStore
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.infrastructure.repository.EmailVerificationRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class EmailVerificationReaderStoreImpl(
    private val emailVerificationRepository: EmailVerificationRepository,
): EmailVerificationReaderStore {

    @Transactional(readOnly = true)
    override fun findEmailVerificationByUser(user: User): EmailVerification? {
        return emailVerificationRepository.findByUser(user)
    }

    @Transactional
    override fun saveEmailVerification(emailVerification: EmailVerification): EmailVerification {
        return emailVerificationRepository.save(emailVerification)
    }

    @Transactional
    override fun deleteEmailVerification(emailVerification: EmailVerification) {
        emailVerificationRepository.delete(emailVerification)
    }
}