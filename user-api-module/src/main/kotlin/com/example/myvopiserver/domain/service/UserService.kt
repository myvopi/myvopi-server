package com.example.myvopiserver.domain.service

import com.commoncoremodule.enums.MemberRole
import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.util.Cipher
import com.commoncoremodule.exception.ErrorCode
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.info.AuthenticationTokenInfo
import com.example.myvopiserver.common.config.authentication.JwtTokenGenerator
import com.example.myvopiserver.domain.QUser
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand
import com.example.myvopiserver.domain.command.UserLoginCommand
import com.example.myvopiserver.domain.command.UserRegisterCommand
import com.example.myvopiserver.domain.mapper.UserMapper
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
    fun registerUser(command: UserRegisterCommand) {
        val userCommand = User(
            name = command.name,
            nationality = command.nationality,
            password = cipher.encrypt(command.password),
            email = command.email,
        )
        userReaderStore.saveUser(userCommand)
    }

    fun updateUserMemberRole(command: InternalUserCommand) {
        userReaderStore.updateUserDslRequest(
            command,
            listOf(UpdateClauseCommand(pathName = QUser.user.role.metadata.name, value = MemberRole.ROLE_USER)),
        )
    }

    // Db-transactions(readOnly)
    fun getUserAndValidateStatus(uuid: String): InternalUserCommand {
        val user = userReaderStore.findUserByUuid(uuid)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
        // Banned status validation
        validationService.validateIfBanned(user.status)
        return userMapper.to(user = user)!!
    }

    fun getUserAndValidateStatusWithRole(uuid: String): InternalUserCommand {
        val user = userReaderStore.findUserByUuid(uuid)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
        // Banned status validation
        validationService.validateIfBanned(user.status)
        // Role validation
        validationService.validateIfIsUserRole(user.role)
        return userMapper.to(user = user)!!
    }

    // Validation & constructors
    fun validateUserRegister(command: UserRegisterCommand) {
        validationService.validateEmail(command.email)
        validationService.validatePasswordFormat(command.password)
        validationService.validateValidCountryCode(command.nationality)
    }

    fun validateUserLogin(command: UserLoginCommand): InternalUserCommand {
        val user = userReaderStore.findUserByEmail(command.email)
            ?: throw BadRequestException(ErrorCode.BAD_REQUEST, "Invalid user credentials")
        val reqPassword = command.password
        val password = user.password
        // Password authentication
        validationService.validatePassword(reqPassword, password)
        // Banned status validation
        validationService.validateIfBanned(user.status)
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

    fun validateIfAuthenticationHasBeenApplied(command: InternalUserCommand?) {
        command?.let {
            validationService.validateIfUserEmailBeenVerified(command.role)
            validationService.validateIfBanned(command.status)
        }
    }
}