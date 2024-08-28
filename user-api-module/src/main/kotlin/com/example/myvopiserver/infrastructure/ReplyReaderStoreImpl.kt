package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.command.SingleReplySearchCommand
import com.example.myvopiserver.domain.interfaces.ReplyReaderStore
import com.example.myvopiserver.infrastructure.custom.queryDsl.repository.ReplyReaderStoreDsl
import com.example.myvopiserver.infrastructure.repository.ReplyRepository
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ReplyReaderStoreImpl(
    private val replyRepository: ReplyRepository,
    private val replyReaderStoreDsl: ReplyReaderStoreDsl,
): ReplyReaderStore {

    @Transactional(readOnly = true)
    override fun findRepliesDslRequest(command: ReplySearchCommand): List<Tuple> {
        return replyReaderStoreDsl.findReplies(command)
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
    override fun findReplyDslRequest(command: SingleReplySearchCommand): Tuple? {
        return replyReaderStoreDsl.findReply(command)
    }
}