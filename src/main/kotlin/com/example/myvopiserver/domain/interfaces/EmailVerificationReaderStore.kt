package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.role.EmailVerification
import com.example.myvopiserver.domain.role.User

interface EmailVerificationReaderStore {

    fun findByUser(user: User): EmailVerification?

    fun saveEmailVerification(emailVerification: EmailVerification): EmailVerification
}