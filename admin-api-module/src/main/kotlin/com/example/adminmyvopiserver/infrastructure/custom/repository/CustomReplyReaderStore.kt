package com.example.adminmyvopiserver.infrastructure.custom.repository

import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import org.springframework.stereotype.Repository

@Repository
interface CustomReplyReaderStore {

    fun updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand)
}