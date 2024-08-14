package com.entitycoremodule.domain.reply

import com.entitycoremodule.command.ReplySearchCommand
import com.entitycoremodule.command.SingleReplySearchCommand
import com.querydsl.core.Tuple

interface ReplyReaderStore {

    fun findRepliesRequest(command: ReplySearchCommand): List<Tuple>

    fun findReplyWithUserByUuid(uuid: String): Reply?

    fun saveReply(reply: Reply): Reply

    fun findReplyByUuid(uuid: String): Reply?

    fun findReplyRequest(command: SingleReplySearchCommand): Tuple?
}