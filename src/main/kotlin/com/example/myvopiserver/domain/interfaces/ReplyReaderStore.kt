package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.querydsl.core.Tuple

interface ReplyReaderStore {

    fun findRepliesRequest(command: ReplySearchCommand): List<Tuple>
}