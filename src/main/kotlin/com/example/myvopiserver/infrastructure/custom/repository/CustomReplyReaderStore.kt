package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.querydsl.core.Tuple
import org.springframework.stereotype.Repository

@Repository
interface CustomReplyReaderStore {

    fun findRepliesRequest(command: ReplySearchCommand): List<Tuple>
}