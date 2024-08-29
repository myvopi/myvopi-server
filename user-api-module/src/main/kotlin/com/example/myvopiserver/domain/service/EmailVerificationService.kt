package com.example.myvopiserver.domain.service

import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.ErrorCode
import com.example.myvopiserver.common.util.CodeGenerator
import com.example.myvopiserver.domain.EmailVerification
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.EmailVerificationCommand
import com.example.myvopiserver.domain.command.EmailVerifyReqCommand
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.interfaces.EmailVerificationReaderStore
import com.example.myvopiserver.domain.mapper.EmailVerificationMapper
import org.springframework.stereotype.Service

@Service
class EmailVerificationService(
    private val emailVerificationReaderStore: EmailVerificationReaderStore,
    private val emailVerificationMapper: EmailVerificationMapper,
    private val validationService: ValidationService,
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
        // 사용자가 이메일 확인 요청을 하지 않았을 때 수작업으로 할 경우
        val emailVerification = emailVerificationReaderStore.findEmailVerificationByUser(user)
            ?: throw BadRequestException(ErrorCode.BAD_REQUEST, "Request a verification code first")
        // 요청 한지 5분이 지났을 때
        validationService.validateIfPast5Minutes(emailVerification.createdDt!!)
        // 한개의 이메일 확인 요청 횟수가 3개 이상 사용 되었을 때
        if(emailVerification.chance == 0) throw BadRequestException(ErrorCode.BAD_REQUEST, "Please request another verification code")
        // 이메일 확인 코드가 맞지 않을 때
        if(command.reqCode != emailVerification.code) {
            emailVerification.removeChance()
            emailVerificationReaderStore.saveEmailVerification(emailVerification)
            throw BadRequestException(ErrorCode.BAD_REQUEST, "Verification code is invalid")
        }
        emailVerificationReaderStore.deleteEmailVerification(emailVerification)
    }
}