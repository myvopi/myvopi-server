package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.BadRequestException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
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
        val emailVerification = emailVerificationReaderStore.findByUser(user)
            ?.apply { setNewCode(newCode) }
            ?: run { EmailVerification(code = newCode, user = user) }
        val storedVerification = emailVerificationReaderStore.saveEmailVerification(emailVerification)
        return emailVerificationMapper.to(
            user = user,
            emailVerification = storedVerification
        )
    }

    // Validation
    fun verifyCode(command: EmailVerifyReqCommand) {
        val user = User(command = command.internalUserCommand)
        val emailVerification = emailVerificationReaderStore.findByUser(user)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        if(command.reqCode != emailVerification.code) throw BadRequestException(ErrorCode.BAD_REQUEST, "Verification code invalid")
    }
}