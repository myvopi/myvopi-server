package com.example.myvopiserver.application.reply

import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.ReplyService
import org.springframework.stereotype.Service

@Service
class ReplyFacade(
    private val replyService: ReplyService,
    private val commentService: CommentService,
) {

    fun requestReplies(command: ReplySearchCommand): List<ReplyBaseInfo> {
        val result = replyService.getReplies(command)
        return replyService.constructReplyBaseInfo(result)
    }

    fun requestReplyUpdate(command: ReplyUpdateCommand): ReplyBaseInfo {
        val internalReplyCommand = replyService.validateAndUpdateContent(command)
        val searchCommand = replyService.constructSingleReplySearchCommand(command, internalReplyCommand)
        val result = replyService.getReply(searchCommand)
        return replyService.constructReplyBaseInfo(result)
    }

    fun requestReplyPost(command: ReplyPostCommand): ReplyBaseInfo {
        val internalCommentCommand = commentService.getCommentRelations(command.commentUuid)
        val internalReplyCommand = replyService.createNewReply(command, internalCommentCommand)
        return replyService.constructInitialReplyBaseInfo(internalReplyCommand)
    }

    fun requestReplyDelete(command: ReplyDeleteCommand) {
        replyService.validateAndDelete(command)
    }

    fun requestReplyLike(command: ReplyLikeCommand) {
        val internalReplyCommand = replyService.getOnlyReply(command.replyUuid)
        replyService.getAndUpdateLikeOrCreateNew(command.internalUserCommand, internalReplyCommand)
    }

    fun requestReplyUnlike(command: ReplyLikeCommand) {
        val internalReplyCommand = replyService.getOnlyReply(command.replyUuid)
        replyService.getAndUpdateUnlike(command.internalUserCommand, internalReplyCommand)
    }

    fun requestReportReply(command: ReplyReportCommand) {
        val internalReplyAndOwnerCommand = replyService.getReplyAndOwnerAndUpdateFlagged(command.replyUuid)
        replyService.validateReportOrStore(command, internalReplyAndOwnerCommand)
    }
}