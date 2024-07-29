package com.example.myvopiserver.application.comment

import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.domain.service.CommentService
import com.example.myvopiserver.domain.service.ValidationService
import org.springframework.stereotype.Service

@Service
class CommentFacade(
    private val commentService: CommentService,
    private val validationService: ValidationService,
    private val commentMapper: CommentMapper,
) {

    fun requestComments(command: CommentSearchFromCommentCommand): List<CommentBaseInfo> {
        val result = commentService.findComments(command)
        return commentService.constructCommentBaseInfo(result)
    }

    fun requestCommentUpdate(command: CommentUpdateCommand): CommentBaseInfo {
        validationService.validateRequestEqualsUser(command.internalUserInfo, command.userId)
        commentService.updateComment(command)
        val searchCommand = SingleCommandSearchCommand(
            userUuid = command.internalUserInfo.uuid,
            userId = command.userId,
            videoId = command.videoId,
            videoType = command.videoType,
            commentUuid = command.commentUuid,
        )
        val result = commentService.findComment(searchCommand)!!
        return commentService.constructCommentBaseInfo(result)
    }

    fun requestCommentPost(command: CommentPostCommand): CommentBaseInfo {
        val internalCommentCommand = commentService.createNewComment(command)
        return commentService.constructCommentBaseInfo(internalCommentCommand)
    }

    fun requestCommentDelete(command: CommentDeleteCommand) {
        validationService.validateRequestEqualsUser(command.internalUserInfo, command.userId)
        val updateCommand = commentMapper.deleteTo(command)
        commentService.updateStatus(updateCommand)
    }
}