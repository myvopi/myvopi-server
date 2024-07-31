package com.example.myvopiserver.application.comment

import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentFacade(
    private val commentService: CommentService,
) {

    fun requestComments(command: CommentSearchFromCommentCommand): List<CommentBaseInfo> {
        val result = commentService.findComments(command)
        return commentService.constructCommentBaseInfo(result)
    }

    fun requestCommentUpdate(command: CommentUpdateCommand): CommentBaseInfo {
        commentService.validateAndUpdateComment(command)
        val searchCommand = SingleCommandSearchCommand(
            internalUserInfo = command.internalUserInfo,
            videoId = command.videoId,
            videoType = command.videoType,
            commentUuid = command.commentUuid,
        )
        val result = commentService.findComment(searchCommand)!!
        return commentService.constructCommentBaseInfo(result)
    }

    fun requestCommentPost(command: CommentPostCommand): CommentBaseInfo {
        val internalCommentCommand = commentService.createNewComment(command)
        return commentService.constructInitialCommentBaseInfo(internalCommentCommand)
    }

    fun requestCommentDelete(command: CommentDeleteCommand) {
        commentService.validateAndUpdateStatus(command)
    }

    fun requestCommentLike(command: CommentLikeCommand) {
        val internalCommentCommand = commentService.findComment(command.commentUuid)
        commentService.searchAndLikeOrCreateNew(command.internalUserInfo, internalCommentCommand)
    }

    fun requestCommentUnlike(command: CommentLikeCommand) {
        val internalCommentCommand = commentService.findComment(command.commentUuid)
        commentService.searchAndUnlike(command.internalUserInfo, internalCommentCommand)
    }
}