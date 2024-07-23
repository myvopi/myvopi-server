package com.example.myvopiserver.common.config.authentication

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.UnauthorizedException
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.role.User
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit


@Component
class JwtTokenGenerator(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val userReaderStore: UserReaderStore,
    private val jwtTokenValidation: JwtTokenValidation
) {
    fun createToken(user: User): String {
        val jwsHeader = JwsHeader.with(MacAlgorithm.HS256)
            .type("JWT")
            .build()
        val claims = JwtClaimsSet.builder()
            .expiresAt(Instant.now().plus(365L, ChronoUnit.DAYS))
            .subject(user.userId)
            .claim("userId", user.userId)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    @Transactional(readOnly = true)
    fun validateTokenAndFindUser(token: String): InternalUserCommand {
        val userId: String = jwtTokenValidation.validateAndDecodeToken(token)
        val user = userReaderStore.findUserByUserId(userId)
            ?: throw UnauthorizedException(ErrorCode.INVALID_TOKEN)

        // TODO
        // if(user.status == RoleStatus.BANNED)

        return InternalUserCommand(
            id = user.id,
            userId = user.userId,
            uuid = user.uuid,
            password = user.password,
            name = user.name,
            nationality = user.nationality,
            email = user.email,
            role = user.role,
        )
    }
}