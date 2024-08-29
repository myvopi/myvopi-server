package com.example.adminmyvopiserver.common.config.authentication

import com.commoncoremodule.enums.TokenType
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.service.AdminService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationManager(
    private val tokenGenerator: JwtTokenGenerator,
    private val adminService: AdminService,
) {
    fun authenticate(jwtToken: String): Authentication {
        val adminUuid = tokenGenerator.decodeAndParse(jwtToken, TokenType.ACCESS_TOKEN)
        val internalUserCommand = adminService.getAdminAndValidateRole(adminUuid)
        return UsernamePasswordAuthenticationToken(internalUserCommand, "", listOf(SimpleGrantedAuthority(internalUserCommand.role.name)))
    }
}

fun Authentication.toUserInfo(): InternalUserCommand {
    return this.principal as InternalUserCommand
}