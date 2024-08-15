package com.example.myvopiserver.application.user

import com.authcoremodule.authentication.JwtTokenGenerator
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.enums.TokenType
import com.entitycoremodule.command.*
import com.entitycoremodule.info.AuthenticationTokenInfo
import com.example.myvopiserver.domain.service.EmailVerificationService
import com.example.myvopiserver.domain.service.UserService
import com.example.myvopiserver.domain.service.ValidationService
import com.externalapimodule.mail.MailService
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val validationService: ValidationService,
    private val userService: UserService,
    private val emailVerificationService: EmailVerificationService,
    private val mailService: MailService,
    private val jwtTokenGenerator: JwtTokenGenerator,
) {

    fun registerUser(command: UserRegisterCommand) {
        validationService.validateUserIdExists(command.userId)
        validationService.validateEmailExists(command.email)
        validationService.validatePasswordFormat(command.password)
        validationService.validateValidCountryCode(command.nationality)
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
        emailVerificationService.verifyCodeAndDeleteVerification(command)
        userService.updateUserMemberRole(command.internalUserCommand)
    }

    fun reissueAccessToken(command: ReissueAccessTokenCommand): AuthenticationTokenInfo {
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