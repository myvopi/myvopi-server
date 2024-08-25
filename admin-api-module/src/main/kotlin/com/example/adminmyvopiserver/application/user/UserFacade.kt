package com.example.adminmyvopiserver.application.user

import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.UserAdminSetRoleStatusCommand
import com.example.adminmyvopiserver.domain.info.UserInfo
import com.example.adminmyvopiserver.domain.mapper.UserMapper
import com.example.adminmyvopiserver.domain.service.CommentService
import com.example.adminmyvopiserver.domain.service.ReplyService
import com.example.adminmyvopiserver.domain.service.UserService
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val commentService: CommentService,
    private val replyService: ReplyService,
) {

    fun requestUsers(command: UserAdminSearchCommand): List<UserInfo> {
        val internalUserCommands = userService.searchUsers(command)
        return internalUserCommands.map { userMapper.ofForAdmin(it) }
    }

    fun requestUserStatusActive(command: UserAdminSetRoleStatusCommand) {
        val internalUserCommand = userService.findUserByUserIdAndUuid(command.userId, command.userUuid)
        userService.setUserStatusActive(internalUserCommand)
    }

    fun requestUserStatusBanned(command: UserAdminSetRoleStatusCommand) {
        val internalUserCommand = userService.setUserStatusBanned(command)
        commentService.deleteAllCommentsDueToBan(internalUserCommand)
        replyService.deleteAllRepliesDueToBan(internalUserCommand)
    }
}