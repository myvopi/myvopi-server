package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.util.exception.BadRequestException
import com.example.myvopiserver.common.util.exception.ErrorCode
import com.example.myvopiserver.common.util.exception.NotFoundException
import com.example.myvopiserver.common.util.CodeGenerator
import com.example.myvopiserver.domain.command.EmailVerificationCommand
import com.example.myvopiserver.domain.command.EmailVerifyReqCommand
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.interfaces.EmailVerificationReaderStore
import com.example.myvopiserver.domain.mapper.EmailVerificationMapper
import com.example.myvopiserver.domain.role.EmailVerification
import com.example.myvopiserver.domain.role.User
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