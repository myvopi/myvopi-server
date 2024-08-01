package com.example.myvopiserver.application.reply

import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import com.example.myvopiserver.domain.service.ReplyService
import org.springframework.stereotype.Service

@Service
class ReplyFacade(
    private val replyService: ReplyService,
) {

    fun requestReplies(command: ReplySearchCommand): List<ReplyBaseInfo> {
        val result = replyService.findReplies(command)
        return replyService.constructReplyBaseInfo(result)
    }
}