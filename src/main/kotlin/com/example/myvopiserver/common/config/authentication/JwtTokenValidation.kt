package com.example.myvopiserver.common.config.authentication

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.UnauthorizedException
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component

@Component
class JwtTokenValidation(
    private val jwtDecoder: JwtDecoder
) {

    fun validateAndDecodeToken(token: String): String {
        try {
            val jwt: Jwt = jwtDecoder.decode(token)
            return jwt.getClaimAsString("userId")
        } catch (e: JwtValidationException) {
            throw UnauthorizedException(ErrorCode.JWT_EXPIRED)
        } catch (e: BadJwtException) {
            throw UnauthorizedException(ErrorCode.JWT_CORRUPT)
        } catch (e: NullPointerException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED)
        }
    }
}