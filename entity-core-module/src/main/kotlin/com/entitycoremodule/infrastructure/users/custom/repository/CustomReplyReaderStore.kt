package com.entitycoremodule.infrastructure.users.custom.repository

import com.entitycoremodule.command.ReplySearchCommand
import com.entitycoremodule.command.SingleReplySearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CustomReplyReaderStore {

    fun findRepliesRequest(command: ReplySearchCommand): List<Tuple>

    fun findReplyRequest(command: SingleReplySearchCommand): Tuple?
}