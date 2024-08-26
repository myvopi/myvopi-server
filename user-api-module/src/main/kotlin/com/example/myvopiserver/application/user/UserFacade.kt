package com.example.myvopiserver.application.user

import com.commoncoremodule.enums.TokenType
import com.example.myvopiserver.common.config.authentication.JwtTokenGenerator
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.AuthenticationTokenInfo
import com.example.myvopiserver.domain.service.EmailVerificationService
import com.example.myvopiserver.domain.service.UserService
import com.externalapimodule.mail.MailService
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val userService: UserService,
    private val emailVerificationService: EmailVerificationService,
    private val mailService: MailService,
    private val jwtTokenGenerator: JwtTokenGenerator,
) {

    fun registerUser(command: UserRegisterCommand) {
        userService.validateUserRegisterRequest(command)
        userService.registerUser(command)
    }

    fun loginUser(command: UserLoginCommand): AuthenticationTokenInfo {
        val internalUserCommand = userService.validateUserLogin(command)
        return userService.createAuthenticationInfo(internalUserCommand)
    }

    fun requestEmailVerificationCode(command: InternalUserCommand) {
        val emailVerificationCommand = emailVerificationService.updateEmailCode(command)
        mailService.sendVerificationEmail(
            id = emailVerificationCommand.id,
            userId = emailVerificationCommand.userId,
            email = emailVerificationCommand.email,
            code = emailVerificationCommand.code,
        )
    }

    fun verifyEmail(command: EmailVerifyReqCommand) {
        emailVerificationService.verifyCodeAndDeleteVerification(command)
        userService.updateUserMemberRole(command.internalUserCommand)
    }

    fun reissueAccessToken(command: ReissueAccessTokenCommand): AuthenticationTokenInfo {
        val userUuid = jwtTokenGenerator.decodeAndParse(command.refreshToken, TokenType.REFRESH_TOKEN)
        val internalUserCommand = userService.getUserAndValidateStatus(userUuid)
        val newAccessToken = jwtTokenGenerator.createAccessToken(internalUserCommand)
        return AuthenticationTokenInfo(
            accessToken = newAccessToken,
            refreshToken = command.refreshToken
        )
    }
}