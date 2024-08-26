package com.example.adminmyvopiserver.domain.interfaces

import com.example.adminmyvopiserver.domain.Reply
import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import org.springframework.data.domain.Pageable

interface ReplyReaderStore {

    fun updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand)

    fun findRepliesByUser(user: User, pageable: Pageable): List<Reply>
}