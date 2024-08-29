package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.EmailVerification
import com.example.myvopiserver.domain.User

interface EmailVerificationReaderStore {

    fun findEmailVerificationByUser(user: User): EmailVerification?

    fun saveEmailVerification(emailVerification: EmailVerification): EmailVerification

    fun deleteEmailVerification(emailVerification: EmailVerification)
}