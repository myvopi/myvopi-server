package com.entitycoremodule.domain.email

import com.entitycoremodule.domain.user.User

interface EmailVerificationReaderStore {

    fun findEmailVerificationByUser(user: User): EmailVerification?

    fun saveEmailVerification(emailVerification: EmailVerification): EmailVerification

    fun deleteEmailVerification(emailVerification: EmailVerification)
}