package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.authentication.JwtTokenGenerator
import com.example.myvopiserver.common.config.exception.BadRequestException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UserLoginCommand
import com.example.myvopiserver.domain.command.UserRegisterCommand
import com.example.myvopiserver.domain.info.AuthenticationTokenInfo
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.role.User
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
    private val jwtTokenGenerator: JwtTokenGenerator,
) {

    fun registerUser(command: UserRegisterCommand): InternalUserCommand {
        val userCommand = User(
            name = command.name,
            userId = command.userId,
            nationality = command.nationality,
            password = command.password, // TODO encrypt
            email = command.email,
        )
        val user = userReaderStore.saveUser(userCommand)
        return userMapper.to(user = user)!!
    }

    fun validateUserLogin(command: UserLoginCommand): AuthenticationTokenInfo {
        val user = userReaderStore.findUserByUserId(command.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)

        // TODO encryption
        val reqPassword = command.password
        val password = user.password
        if(reqPassword != password) throw BadRequestException(ErrorCode.BAD_REQUEST, "Bad request")
        val internalUserCommand = userMapper.to(user = user)!!
        return AuthenticationTokenInfo(
            accessToken = jwtTokenGenerator.createAccessToken(internalUserCommand),
            refreshToken = jwtTokenGenerator.createRefreshToken(internalUserCommand),
        )
    }

    fun updateUserMemberRole(command: InternalUserCommand) {
        val user = userReaderStore.findUserByUserId(command.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        user.setMemberRoleUser()
        userReaderStore.saveUser(user)
    }
}