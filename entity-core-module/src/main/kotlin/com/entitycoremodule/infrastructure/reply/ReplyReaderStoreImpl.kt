package com.entitycoremodule.infrastructure.reply

import com.entitycoremodule.command.ReplySearchCommand
import com.entitycoremodule.command.SingleReplySearchCommand
import com.entitycoremodule.domain.reply.Reply
import com.entitycoremodule.domain.reply.ReplyReaderStore
import com.entitycoremodule.infrastructure.custom.repository.CustomReplyReaderStore
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

    @Transactional(readOnly = true)
    override fun findReplyWithUserByUuid(uuid: String): Reply? {
        return replyRepository.findWithUserByUuid(uuid)
    }

    @Transactional
    override fun saveReply(reply: Reply): Reply {
        return replyRepository.save(reply)
    }

    @Transactional(readOnly = true)
    override fun findReplyByUuid(uuid: String): Reply? {
        return replyRepository.findByUuid(uuid)
    }

    @Transactional(readOnly = true)
    override fun findReplyRequest(command: SingleReplySearchCommand): Tuple? {
        return customReplyReaderStore.findReplyRequest(command)
    }
}