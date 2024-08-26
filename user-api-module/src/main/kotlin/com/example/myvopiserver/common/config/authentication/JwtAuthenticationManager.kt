package com.example.myvopiserver.common.config.authentication

import com.commoncoremodule.enums.TokenType
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.example.myvopiserver.common.util.UrlObject
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationManager(
    private val tokenGenerator: JwtTokenGenerator,
    private val userService: UserService,
    private val request: HttpServletRequest,
    urlObject: UrlObject,
): AuthenticationManager {

    private val requestMatchers = urlObject.requestMatchers()

    override fun authenticate(authentication: Authentication): Authentication {
        val uri = request.requestURI
        val registeredMethods = requestMatchers[uri] ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val method = request.method
        registeredMethods.find { it == method } ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        if(uri.contains("/cv")) {
            val jwt = authentication as BearerTokenAuthenticationToken
            val validatedToken = tokenGenerator.decodeToken(jwt.token)
            val userUuid = tokenGenerator.parseToken(validatedToken, TokenType.ACCESS_TOKEN)
            val internalUserCommand = userService.getUserAndValidateStatus(userUuid)
            return UsernamePasswordAuthenticationToken(internalUserCommand, "", listOf(SimpleGrantedAuthority(internalUserCommand.role.name)))
        } else throw NotFoundException(ErrorCode.NOT_FOUND)
    }
}

fun Authentication.toUserInfo(): InternalUserCommand {
    return this.principal as InternalUserCommand
}