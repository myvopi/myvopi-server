package com.example.adminmyvopiserver.application.admin

import com.commoncoremodule.enums.TokenType
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.example.adminmyvopiserver.common.config.authentication.JwtTokenGenerator
import com.example.adminmyvopiserver.domain.command.AdminLoginCommand
import com.example.adminmyvopiserver.domain.command.ReissueAccessTokenCommand
import com.example.adminmyvopiserver.domain.info.AuthenticationTokenInfo
import com.example.adminmyvopiserver.domain.service.AdminService
import org.springframework.stereotype.Service

@Service
class AdminFacade(
    private val adminService: AdminService,
    private val jwtTokenGenerator: JwtTokenGenerator,
) {

    fun loginAdmin(command: AdminLoginCommand): AuthenticationTokenInfo {
        val internalUserCommand = adminService.validateAdminLogin(command)
        return adminService.createAuthenticationInfo(internalUserCommand)
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