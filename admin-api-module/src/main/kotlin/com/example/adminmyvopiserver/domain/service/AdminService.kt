package com.example.adminmyvopiserver.domain.service

import com.commoncoremodule.enums.MemberRole
import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.util.Cipher
import com.example.adminmyvopiserver.domain.command.AdminLoginCommand
import com.example.adminmyvopiserver.common.config.authentication.JwtTokenGenerator
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.info.AuthenticationTokenInfo
import com.example.adminmyvopiserver.domain.interfaces.UserReaderStore
import com.example.adminmyvopiserver.domain.mapper.UserMapper
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val cipher: Cipher,
) {

    // Validation and constructor
    fun validateAdminLogin(command: AdminLoginCommand): InternalUserCommand {
        val user = userReaderStore.findUserByUserId(command.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val reqPassword = command.password
        val password = user.password
        val decryptedPassword = cipher.decrypt(password)
        // Password authentication
        if(reqPassword != decryptedPassword) throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad request")
        // Role authentication
        if(user.role != MemberRole.ROLE_ADMIN) throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad request")
        return userMapper.to(user = user)!!
    }

    fun getAdminAndValidateRole(uuid: String): InternalUserCommand {
        val user = userReaderStore.findUserByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        // Role authentication
        if(user.role != MemberRole.ROLE_ADMIN) throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad request")
        return userMapper.to(user = user)!!
    }

    fun createAuthenticationInfo(
        internalUserCommand: InternalUserCommand
    ): AuthenticationTokenInfo {
        return AuthenticationTokenInfo(
            accessToken = jwtTokenGenerator.createAccessToken(internalUserCommand),
            refreshToken = jwtTokenGenerator.createRefreshToken(internalUserCommand),
        )
    }
}