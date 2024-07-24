package com.example.myvopiserver.application.user

import com.example.myvopiserver.domain.command.EmailVerifyReqCommand
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UserLoginCommand
import com.example.myvopiserver.domain.command.UserRegisterCommand
import com.example.myvopiserver.domain.service.EmailVerificationService
import com.example.myvopiserver.domain.service.UserService
import com.example.myvopiserver.domain.service.ValidationService
import com.example.myvopiserver.external.MailService
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val validationService: ValidationService,
    private val userService: UserService,
    private val emailVerificationService: EmailVerificationService,
    private val mailService: MailService,
) {

    /**
     * 회원가입
     * 1. 아이디 중복 검사
     * 2. 이메일 중복 검사
     * 3. 비밀번호 유효 검사
     * 4. 요청 국적 코드 검사
     * 5. 계정 생성
     * */
    fun registerUser(command: UserRegisterCommand) {
        validationService.validateUserId(command.userId)
        validationService.validateEmail(command.email)
        validationService.validateFormatPassword(command.password)
        validationService.validateCountryCode(command.nationality)

        userService.registerUser(command)
    }

    fun loginUser(command: UserLoginCommand): String {
        return userService.validateUserLogin(command)
    }

    fun requestEmailVerificationCode(command: InternalUserCommand) {
        val emailVerificationCommand = emailVerificationService.updateEmailCode(command)
        mailService.sendVerificationEmail(emailVerificationCommand)
    }

    fun verifyEmail(command: EmailVerifyReqCommand) {
        emailVerificationService.verifyCode(command)
        userService.updateUserMemberRole(command.internalUserCommand)
    }
}