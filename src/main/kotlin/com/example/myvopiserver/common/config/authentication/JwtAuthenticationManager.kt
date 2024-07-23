package com.example.myvopiserver.common.config.authentication

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
        val internalUserCommand = tokenGenerator.validateTokenAndFindUser(token)
        val grantedAuthorityList = mutableListOf<SimpleGrantedAuthority>()
        grantedAuthorityList.add(SimpleGrantedAuthority(internalUserCommand.role.name))
        return UsernamePasswordAuthenticationToken(internalUserCommand, "", grantedAuthorityList)
    }
}