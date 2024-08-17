package com.entitycoremodule.domain.interfaces.users

import com.entitycoremodule.domain.email.EmailVerification
import com.entitycoremodule.domain.user.User

interface EmailVerificationReaderStore {

    fun findEmailVerificationByUser(user: User): EmailVerification?

    fun saveEmailVerification(emailVerification: EmailVerification): EmailVerification

    fun deleteEmailVerification(emailVerification: EmailVerification)
}