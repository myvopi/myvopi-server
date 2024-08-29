package com.example.adminmyvopiserver.application.admin

import com.commoncoremodule.enums.TokenType
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
        val adminUuid = jwtTokenGenerator.decodeAndParse(command.refreshToken, TokenType.REFRESH_TOKEN)
        val internalUserCommand = adminService.getAdminAndValidateRole(adminUuid)
        val newAccessToken = jwtTokenGenerator.createAccessToken(internalUserCommand)
        return AuthenticationTokenInfo(
            accessToken = newAccessToken,
            refreshToken = command.refreshToken
        )
    }
}