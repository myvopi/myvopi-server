package com.example.adminmyvopiserver.domain.service

import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.interfaces.ReplyReaderStore
import org.springframework.stereotype.Service

@Service
class ReplyService(
    private val replyReaderStore: ReplyReaderStore,
) {

    // Db-transactions
    fun deleteAllRepliesDueToBan(internalUserCommand: InternalUserCommand) {
        replyReaderStore.updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand)
    }

}