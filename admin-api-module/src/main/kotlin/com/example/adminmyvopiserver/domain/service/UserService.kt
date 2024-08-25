package com.example.adminmyvopiserver.domain.service

import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.interfaces.UserReaderStore
import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.UserAdminSetRoleStatusCommand
import com.example.adminmyvopiserver.domain.mapper.UserMapper
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReaderStore: UserReaderStore,
    private val userMapper: UserMapper,
) {

    // Db-transactions (readOnly)
    fun searchUsers(command: UserAdminSearchCommand): List<InternalUserCommand> {
        val results = userReaderStore.searchUsersRequest(command)
        return results.map { userMapper.toForAdmin(it) }
    }

    fun findUserByUserIdAndUuid(userId: String, uuid: String): InternalUserCommand {
        val user = userReaderStore.findUserByUserIdAndUuid(userId, uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No user exists")
        return userMapper.to(user)!!
    }

    // Db-transactions
    fun setUserStatusActive(
        internalUserCommand: InternalUserCommand
    ) {
        val user = User(command = internalUserCommand)
            .apply { setRoleStatusActive() }
        userReaderStore.saveUser(user)
    }

    fun setUserStatusBanned(
        command: UserAdminSetRoleStatusCommand
    ): InternalUserCommand {
        val user = userReaderStore.findUserByUserIdAndUuid(command.userId, command.userUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND, "No user exists")
        user.setRoleStatusBanned()
        val savedUser = userReaderStore.saveUser(user)
        return userMapper.to(savedUser)!!
    }
}