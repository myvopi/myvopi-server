package com.example.myvopiserver.infrastructure.custom.queryDsl.repository

import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.command.SingleReplySearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface ReplyReaderStoreDsl {

    fun findReplies(command: ReplySearchCommand): List<Tuple>

    fun findReply(command: SingleReplySearchCommand): Tuple?
}