package com.example.myvopiserver.application.comment

import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.VideoService
import org.springframework.stereotype.Service

@Service
class CommentFacade(
    private val commentService: CommentService,
    private val videoService: VideoService,
) {

    fun requestComments(command: CommentSearchFromCommentCommand): List<CommentBaseInfo> {
        val result = commentService.findComments(command)
        return commentService.constructCommentBaseInfo(result)
    }

    fun requestCommentUpdate(command: CommentUpdateCommand): CommentBaseInfo {
        commentService.validateAndUpdateContent(command)
        val searchCommand = commentService.constructSingleCommentSearchCommand(command)
        val result = commentService.findComment(searchCommand)
        return commentService.constructCommentBaseInfo(result)
    }

    fun requestCommentPost(command: CommentPostCommand): CommentBaseInfo {
        val internalVideoCommand = videoService.findVideoWithOwner(command.videoType, command.videoId)
        val internalCommentCommand = commentService.createNewComment(command, internalVideoCommand)
        return commentService.constructInitialCommentBaseInfo(internalCommentCommand)
    }

    fun requestCommentDelete(command: CommentDeleteCommand) {
        commentService.validateAndUpdateStatus(command)
    }

    fun requestCommentLike(command: CommentLikeCommand) {
        val internalCommentCommand = commentService.findOnlyComment(command.commentUuid)
        commentService.searchAndUpdateLikeOrCreateNew(command.internalUserCommand, internalCommentCommand)
    }

    fun requestCommentUnlike(command: CommentLikeCommand) {
        val internalCommentCommand = commentService.findOnlyComment(command.commentUuid)
        commentService.searchAndUpdateUnlike(command.internalUserCommand, internalCommentCommand)
    }
}