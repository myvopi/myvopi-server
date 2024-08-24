package com.example.adminmyvopiserver.application.user

import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSetRoleStatusCommand
import com.example.adminmyvopiserver.domain.info.UserInfo
import com.example.adminmyvopiserver.domain.mapper.UserMapper
import com.example.adminmyvopiserver.domain.service.UserService
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    fun requestUsers(command: UserAdminSearchCommand): List<UserInfo> {
        val internalUserCommands = userService.searchUsers(command)
        return internalUserCommands.map { userMapper.ofForAdmin(it) }
    }

    fun requestUserStatus(command: UserAdminSetRoleStatusCommand) {
        val internalUserCommand = userService.findUserByUserIdAndUuid(command.userId, command.userUuid)
        userService.setUserStatus(command.status, internalUserCommand)
    }
}