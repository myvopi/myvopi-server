package com.example.myvopiserver.domain.service

import com.authcoremodule.authentication.JwtTokenGenerator
import com.commoncoremodule.util.Cipher
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.entitycoremodule.command.InternalUserCommand
import com.entitycoremodule.command.UserLoginCommand
import com.entitycoremodule.command.UserRegisterCommand
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.domain.user.UserReaderStore
import com.entitycoremodule.info.AuthenticationTokenInfo
import com.entitycoremodule.mapper.common.UserMapper
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val validationService: ValidationService,
    private val cipher: Cipher,
) {

    // Db-transactions
    fun registerUser(command: UserRegisterCommand): InternalUserCommand {
        val userCommand = User(
            name = command.name,
            userId = command.userId,
            nationality = command.nationality,
            password = cipher.encrypt(command.password),
            email = command.email,
        )
        val user = userReaderStore.saveUser(userCommand)
        return userMapper.to(user = user)!!
    }

    fun updateUserMemberRole(command: InternalUserCommand) {
        val user = User(command)
        user.setMemberRoleUser()
        userReaderStore.saveUser(user)
    }

    // Validation
    fun validateUserLogin(command: UserLoginCommand): AuthenticationTokenInfo {
        val user = userReaderStore.findUserByUserId(command.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val reqPassword = command.password
        val password = user.password
        validationService.validatePassword(reqPassword, password)
        val internalUserCommand = userMapper.to(user = user)!!
        return AuthenticationTokenInfo(
            accessToken = jwtTokenGenerator.createAccessToken(internalUserCommand),
            refreshToken = jwtTokenGenerator.createRefreshToken(internalUserCommand),
        )
    }
}