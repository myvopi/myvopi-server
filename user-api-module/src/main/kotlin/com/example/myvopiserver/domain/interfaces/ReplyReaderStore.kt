package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.command.SingleReplySearchCommand
import com.querydsl.core.Tuple

interface ReplyReaderStore {

    fun findRepliesDslRequest(command: ReplySearchCommand): List<Tuple>

    fun findReplyWithUserByUuid(uuid: String): Reply?

    fun findReplyWithUserAndCommentByUuid(uuid: String): Reply?

    fun saveReply(reply: Reply): Reply

    fun findReplyByUuid(uuid: String): Reply?

    fun findReplyDslRequest(command: SingleReplySearchCommand): Tuple?
}