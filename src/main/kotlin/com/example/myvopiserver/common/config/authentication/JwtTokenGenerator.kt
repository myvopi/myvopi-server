package com.example.myvopiserver.common.config.authentication

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.UnauthorizedException
import com.example.myvopiserver.common.enums.TokenType
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.mapper.UserMapper
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtTokenGenerator(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
) {

    private final val accessTokenExpireTime = 1L
    private final val refreshTokenExpireDate = 1L

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
            userMapper.to(user = userReaderStore.findUserByUuid(uuid))
        } catch (e: JwtException) {
            null
        }
    }

    @Transactional(readOnly = true)
    fun parseRefreshToken(
        jwt: Jwt
    ): InternalUserCommand?
    {
        return try{
            val key = jwt.subject
            userMapper.to(user = userReaderStore.findUserByUuid(key))
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