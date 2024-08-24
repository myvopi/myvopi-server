package com.example.adminmyvopiserver.common.config.authentication

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationManager(
    private val tokenGenerator: JwtTokenGenerator
): AuthenticationManager
{
    override fun authenticate(authentication: Authentication): Authentication {
        val jwt = authentication as BearerTokenAuthenticationToken
        val token = jwt.token
        val internalUserCommand = tokenGenerator.parseAccessToken(token)
            ?: throw UnauthorizedException(ErrorCode.INVALID_TOKEN)
        return UsernamePasswordAuthenticationToken(internalUserCommand, "", listOf(SimpleGrantedAuthority(internalUserCommand.role.name)))
    }
}

fun Authentication.toUserInfo(): InternalUserCommand {
    return this.principal as InternalUserCommand
}