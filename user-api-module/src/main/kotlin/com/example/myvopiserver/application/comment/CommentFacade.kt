package com.example.myvopiserver.application.comment

import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.ValidationService
import com.example.myvopiserver.domain.service.VideoService
import org.springframework.stereotype.Service

@Service
class CommentFacade(
    private val commentService: CommentService,
    private val videoService: VideoService,
    private val validationService: ValidationService,
) {

    fun requestComments(command: CommentsSearchCommand): List<CommentBaseInfo> {
        val result = commentService.getCommentsRequest(command)
        return commentService.constructCommentBaseInfo(result, command.preferences!!)
    }

    fun requestCommentUpdate(command: CommentUpdateCommand): CommentBaseInfo {
        val internalCommentCommand = commentService.validateAndUpdateContent(command)
        val searchCommand = commentService.constructSingleCommentSearchCommand(command, internalCommentCommand)
        val result = commentService.getComment(searchCommand)
        return commentService.constructCommentBaseInfo(result, command.preferences)
    }

    fun requestCommentPost(command: CommentPostCommand): CommentBaseInfo {
        validationService.validateContentSize(command.content)
        val internalVideoCommand = videoService.getVideoWithOwner(command.videoType, command.videoId)
        val internalCommentCommand = commentService.createNewComment(command, internalVideoCommand)
        return commentService.constructInitialCommentBaseInfo(internalCommentCommand, command.preferences)
    }

    fun requestCommentDelete(command: CommentDeleteCommand) {
        commentService.validateAndDelete(command)
    }

    fun requestCommentLike(command: CommentLikeCommand) {
        val internalCommentCommand = commentService.getOnlyComment(command.commentUuid)
        commentService.getAndUpdateLikeOrCreateNew(command.internalUserCommand, internalCommentCommand)
    }

    fun requestCommentUnlike(command: CommentLikeCommand) {
        val internalCommentCommand = commentService.getOnlyComment(command.commentUuid)
        commentService.getAndUpdateUnlike(command.internalUserCommand, internalCommentCommand)
    }

    fun requestReportComment(command: CommentReportCommand) {
        val internalCommentAndOwnerCommand = commentService.getCommentAndOwnerAndUpdateFlagged(command.commentUuid)
        commentService.validateReportOrStore(command, internalCommentAndOwnerCommand)
    }
}