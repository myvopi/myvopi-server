package com.example.myvopiserver.application.user

import com.example.myvopiserver.common.config.authentication.JwtTokenGenerator
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.UnauthorizedException
import com.example.myvopiserver.common.enums.TokenType
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.AuthenticationTokenInfo
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
    private val jwtTokenGenerator: JwtTokenGenerator,
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

    fun loginUser(command: UserLoginCommand): AuthenticationTokenInfo {
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

    fun reissueAccessToken(command: ReissueAccessTokenCommand, ): AuthenticationTokenInfo {
        val validatedRefreshToken = jwtTokenGenerator.parseTokenFilter(command.refreshToken, TokenType.REFRESH_TOKEN)
        val internalUserCommand = jwtTokenGenerator.parseRefreshToken(validatedRefreshToken)
            ?: throw UnauthorizedException(ErrorCode.INVALID_TOKEN)
        val newAccessToken = jwtTokenGenerator.createAccessToken(internalUserCommand)
        return AuthenticationTokenInfo(
            accessToken = newAccessToken,
            refreshToken = command.refreshToken
        )
    }
}