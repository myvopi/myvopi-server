package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.interfaces.ReplyReaderStore
import com.example.myvopiserver.infrastructure.custom.repository.CustomReplyReaderStore
import com.example.myvopiserver.infrastructure.repository.ReplyRepository
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ReplyReaderStoreImpl(
    private val replyRepository: ReplyRepository,
    private val customReplyReaderStore: CustomReplyReaderStore,
): ReplyReaderStore {

    @Transactional(readOnly = true)
    override fun findRepliesRequest(command: ReplySearchCommand): List<Tuple> {
        return customReplyReaderStore.findRepliesRequest(command)
    }
}