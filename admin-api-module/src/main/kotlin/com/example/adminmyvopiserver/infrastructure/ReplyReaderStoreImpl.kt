package com.example.adminmyvopiserver.infrastructure

import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.interfaces.ReplyReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomReplyReaderStore
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ReplyReaderStoreImpl(
    private val customReplyReaderStore: CustomReplyReaderStore,
): ReplyReaderStore {

    @Transactional
    override fun updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand) {
        customReplyReaderStore.updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand)
    }


}