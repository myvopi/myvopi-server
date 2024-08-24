package com.example.myvopiserver.common.config.authentication

import com.commoncoremodule.enums.RoleStatus
import com.commoncoremodule.exception.BannedException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.example.myvopiserver.domain.command.InternalUserCommand
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
        // TODO need to return banned status somewhere
        if(internalUserCommand.status == RoleStatus.BANNED) throw BannedException(ErrorCode.BANNED)
        return UsernamePasswordAuthenticationToken(internalUserCommand, "", listOf(SimpleGrantedAuthority(internalUserCommand.role.name)))
    }
}

fun Authentication.toUserInfo(): InternalUserCommand {
    return this.principal as InternalUserCommand
}