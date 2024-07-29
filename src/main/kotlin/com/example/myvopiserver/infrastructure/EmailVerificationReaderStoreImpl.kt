package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.EmailVerificationReaderStore
import com.example.myvopiserver.domain.role.EmailVerification
import com.example.myvopiserver.domain.role.User
import com.example.myvopiserver.infrastructure.repository.EmailVerificationRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class EmailVerificationReaderStoreImpl(
    private val emailVerificationRepository: EmailVerificationRepository,
): EmailVerificationReaderStore {

    @Transactional(readOnly = true)
    override fun findByUser(user: User): EmailVerification? {
        return emailVerificationRepository.findByUser(user)
    }

    @Transactional
    override fun saveEmailVerification(emailVerification: EmailVerification): EmailVerification {
        return emailVerificationRepository.save(emailVerification)
    }
}