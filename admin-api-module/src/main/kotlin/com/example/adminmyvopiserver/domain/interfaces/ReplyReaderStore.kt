package com.example.adminmyvopiserver.domain.interfaces

import com.example.adminmyvopiserver.domain.command.InternalUserCommand

interface ReplyReaderStore {

    fun updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand)
}