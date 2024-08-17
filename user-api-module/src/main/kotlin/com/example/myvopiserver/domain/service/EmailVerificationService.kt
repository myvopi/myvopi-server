package com.example.myvopiserver.domain.service

import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.example.myvopiserver.common.util.CodeGenerator
import com.entitycoremodule.command.EmailVerificationCommand
import com.entitycoremodule.command.EmailVerifyReqCommand
import com.entitycoremodule.command.InternalUserCommand
import com.entitycoremodule.domain.email.EmailVerification
import com.entitycoremodule.domain.interfaces.users.EmailVerificationReaderStore
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.mapper.email.EmailVerificationMapper
import org.springframework.stereotype.Service

@Service
class EmailVerificationService(
    private val emailVerificationReaderStore: EmailVerificationReaderStore,
    private val emailVerificationMapper: EmailVerificationMapper,
) {

    // Db-transactions
    fun updateEmailCode(command: InternalUserCommand): EmailVerificationCommand {
        val newCode = CodeGenerator.sixDigitCode()
        val user = User(command = command)
        val emailVerification = emailVerificationReaderStore.findEmailVerificationByUser(user)
            ?.apply { setNewCode(newCode) }
            ?: run { EmailVerification(code = newCode, user = user) }
        val storedVerification = emailVerificationReaderStore.saveEmailVerification(emailVerification)
        return emailVerificationMapper.to(
            user = user,
            emailVerification = storedVerification
        )
    }

    // Validation
    fun verifyCodeAndDeleteVerification(command: EmailVerifyReqCommand) {
        val user = User(command = command.internalUserCommand)
        val emailVerification = emailVerificationReaderStore.findEmailVerificationByUser(user)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "Request a verification code first")
        if(emailVerification.chance == 0) throw BadRequestException(ErrorCode.BAD_REQUEST, "Please request another verification code")
        if(command.reqCode != emailVerification.code) {
            emailVerification.removeChance()
            emailVerificationReaderStore.saveEmailVerification(emailVerification)
            throw BadRequestException(ErrorCode.BAD_REQUEST, "Verification code invalid")
        }
        emailVerificationReaderStore.deleteEmailVerification(emailVerification)
    }
}