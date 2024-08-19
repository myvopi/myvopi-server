package com.example.myvopiserver.application.reply

import com.entitycoremodule.command.*
import com.entitycoremodule.info.ReplyBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.ReplyService
import org.springframework.stereotype.Service

@Service
class ReplyFacade(
    private val replyService: ReplyService,
    private val commentService: CommentService,
) {

    fun requestReplies(command: ReplySearchCommand): List<ReplyBaseInfo> {
        val result = replyService.findReplies(command)
        return replyService.constructReplyBaseInfo(result)
    }

    fun requestReplyUpdate(command: ReplyUpdateCommand): ReplyBaseInfo {
        replyService.validateAndUpdateContent(command)
        val searchCommand = replyService.constructSingleReplySearchCommand(command)
        val result = replyService.findReply(searchCommand)
        return replyService.constructReplyBaseInfo(result)
    }

    fun requestReplyPost(command: ReplyPostCommand): ReplyBaseInfo {
        val internalCommentCommand = commentService.findCommentRelations(command.commentUuid)
        val internalReplyCommand = replyService.createNewReply(command, internalCommentCommand)
        return replyService.constructInitialReplyBaseInfo(internalReplyCommand)
    }

    fun requestReplyDelete(command: ReplyDeleteCommand) {
        replyService.validateAndUpdateStatus(command)
    }

    fun requestReplyLike(command: ReplyLikeCommand) {
        val internalReplyCommand = replyService.findOnlyReply(command.replyUuid)
        replyService.searchAndUpdateLikeOrCreateNew(command.internalUserCommand, internalReplyCommand)
    }

    fun requestReplyUnlike(command: ReplyLikeCommand) {
        val internalReplyCommand = replyService.findOnlyReply(command.replyUuid)
        replyService.searchAndUpdateUnlike(command.internalUserCommand, internalReplyCommand)
    }

    fun requestReportReply(command: ReplyReportCommand) {
        val internalReplyAndOwnerCommand = replyService.findReplyAndOwnerAndUpdateFlagged(command.replyUuid)
        replyService.validateReportOrStore(command, internalReplyAndOwnerCommand)
    }
}