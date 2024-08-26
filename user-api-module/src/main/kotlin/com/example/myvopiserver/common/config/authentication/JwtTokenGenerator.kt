package com.example.myvopiserver.common.config.authentication

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.enums.TokenType
import com.example.myvopiserver.domain.command.InternalUserCommand
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtTokenGenerator(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
) {

    private val accessTokenExpireTime = 1L
    private val refreshTokenExpireDate = 1L

    fun createAccessToken(command: InternalUserCommand): String {
        val jwsHeader = JwsHeader.with{"HS256"}.build()
        val uniqueId = UUID.randomUUID().toString()
        val claims = JwtClaimsSet.builder()
            .expiresAt(Instant.now().plus(accessTokenExpireTime, ChronoUnit.HOURS))
            .claim("unique", command.uuid)
            .subject(uniqueId)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun createRefreshToken(command: InternalUserCommand): String {
        val jwsHeader = JwsHeader.with{"HS256"}.build()
        val uniqueId = UUID.randomUUID().toString()
        val claims = JwtClaimsSet.builder()
            .expiresAt(Instant.now().plus(refreshTokenExpireDate, ChronoUnit.DAYS))
            .issuedAt(Instant.now())
            .claim("unique", uniqueId)
            .subject(command.uuid)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun decodeToken(token: String): Jwt {
        return jwtDecoder.decode(token)
    }

    fun parseToken(jwt: Jwt, tokenType: TokenType): String {
        return if(tokenType == TokenType.ACCESS_TOKEN) jwt.claims["unique"]?.let { it as String }!!
        else jwt.subject
    }

    fun decodeAndParse(token: String, tokenType: TokenType): String {
        try {
            val validatedJwt = decodeToken(token)
            return parseToken(validatedJwt, tokenType)
        } catch (e: JwtValidationException) {
            when(tokenType) {
                TokenType.ACCESS_TOKEN -> throw UnauthorizedException(ErrorCode.EXPIRED_TOKEN)
                TokenType.REFRESH_TOKEN -> throw UnauthorizedException(ErrorCode.REFRESH_TOKEN_EXPIRED)
            }
        } catch (e: BadJwtException) {
            throw UnauthorizedException(ErrorCode.JWT_CORRUPT, ErrorCode.JWT_CORRUPT.engErrorMsg + " value 1")
        } catch (e: NullPointerException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.engErrorMsg + " value 2")
        } catch (e: JwtException) {
            throw UnauthorizedException(ErrorCode.JWT_CORRUPT, ErrorCode.JWT_CORRUPT.engErrorMsg + " value 3")
        }
    }
}