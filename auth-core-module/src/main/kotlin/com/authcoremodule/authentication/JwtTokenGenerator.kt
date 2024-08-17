package com.authcoremodule.authentication

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.UnauthorizedException
import com.commoncoremodule.enums.TokenType
import com.entitycoremodule.command.InternalUserCommand
import com.entitycoremodule.domain.interfaces.users.UserReaderStore
import com.entitycoremodule.mapper.common.UserMapper
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtTokenGenerator(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
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

    fun parseAccessToken(
        token: String
    ): InternalUserCommand?
    {
        return try{
            val jwt = this.parseTokenFilter(token, TokenType.ACCESS_TOKEN)
            val claims = jwt.claims
            val uuid = claims["unique"] as String
            val user = userReaderStore.findUserByUuid(uuid)
            userMapper.to(user = user)
        } catch (e: JwtException) {
            null
        }
    }

    fun parseRefreshToken(
        jwt: Jwt
    ): InternalUserCommand?
    {
        return try{
            val key = jwt.subject
            val user = userReaderStore.findUserByUuid(key)
            userMapper.to(user = user)
        } catch (e: JwtException) {
            null
        }
    }

    fun parseTokenFilter(token: String, tokenType: TokenType): Jwt {
        try{
            return decodeToken(token)
        }
        catch (e: JwtValidationException) {
            when(tokenType) {
                TokenType.ACCESS_TOKEN -> throw UnauthorizedException(ErrorCode.EXPIRED_TOKEN)
                TokenType.REFRESH_TOKEN -> throw UnauthorizedException(ErrorCode.REFRESH_TOKEN_EXPIRED)
            }
        } catch (e: BadJwtException) {
            throw UnauthorizedException(ErrorCode.JWT_CORRUPT)
        } catch (e: NullPointerException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED)
        }
    }

    fun parseTokenFilter(token: String): Jwt {
        try {
            return decodeToken(token)
        }
        catch (e: JwtValidationException) {
            throw UnauthorizedException(ErrorCode.EXPIRED_TOKEN)
        } catch (e: BadJwtException) {
            throw UnauthorizedException(ErrorCode.JWT_CORRUPT)
        } catch (e: NullPointerException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED)
        }
    }

    fun decodeToken(token: String): Jwt {
        return jwtDecoder.decode(token)
    }
}