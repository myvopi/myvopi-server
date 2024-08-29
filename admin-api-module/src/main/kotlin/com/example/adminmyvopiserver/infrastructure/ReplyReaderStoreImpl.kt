package com.example.adminmyvopiserver.infrastructure

import com.example.adminmyvopiserver.domain.Reply
import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.interfaces.ReplyReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomReplyReaderStore
import com.example.adminmyvopiserver.infrastructure.repository.ReplyRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ReplyReaderStoreImpl(
    private val customReplyReaderStore: CustomReplyReaderStore,
    private val replyRepository: ReplyRepository,
): ReplyReaderStore {

    @Transactional
    override fun updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand) {
        customReplyReaderStore.updateRepliesStatusDeleteAdminByUserRequest(internalUserCommand)
    }

    @Transactional(readOnly = true)
    override fun findRepliesByUser(user: User, pageable: Pageable): List<Reply> {
        return replyRepository.findByUser(user, pageable)
    }
}