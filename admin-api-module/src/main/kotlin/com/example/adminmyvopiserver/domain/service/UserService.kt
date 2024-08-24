package com.example.adminmyvopiserver.domain.service

import com.commoncoremodule.enums.RoleStatus
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.interfaces.UserReaderStore
import com.example.adminmyvopiserver.domain.User
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
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return userMapper.to(user)!!
    }

    // Db-transactions
    fun setUserStatus(
        reqStatus: RoleStatus,
        internalUserCommand: InternalUserCommand
    ): InternalUserCommand
    {
        val user = User(command = internalUserCommand)
            .apply {
                if(reqStatus == RoleStatus.ACTIVE) setRoleStatusActive()
                else setRoleStatusBanned()
            }
        val savedUser = userReaderStore.saveUser(user)
        return userMapper.to(savedUser)!!
    }

}