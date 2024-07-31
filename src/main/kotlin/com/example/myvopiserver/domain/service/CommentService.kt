package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.BaseException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.infrastructure.custom.alias.BasicAlias
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val alias: BasicAlias,
    private val commentMapper: CommentMapper,
    private val userReaderStore: UserReaderStore,
    private val videoReaderStore: VideoReaderStore,
    private val likeReaderStore: LikeReaderStore,
) {

    private fun mapCommentBaseInfoOfResult(result: Tuple): CommentBaseInfo {
        return CommentBaseInfo(
            commentUuid = result.get(alias.columnCommentUuid)!!,
            content = result.get(alias.columnCommentContent)!!,
            userId = result.get(alias.columnUserId)!!,
            likeCount = result.get(alias.columnCommentLikesCount)!!,
            replyCount = result.get(alias.columnReplyCount)!!,
            createdDate = result.get(alias.columnCreatedDateTuple)!!.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = result.get(alias.columnCommentModifiedCnt)!! > 0,
            userLiked = result.get(alias.columnUserLiked)?: false,
        )
    }

    fun findCommentsFromVideo(command: CommentSearchFromVideoCommand): List<Tuple> {
        return commentReaderStore.findCommentsFromVideoRequest(command)
    }

    fun findComments(command: CommentSearchFromCommentCommand): List<Tuple> {
        return commentReaderStore.findCommentsFromCommentRequest(command)
    }

    fun constructCommentBaseInfo(results: List<Tuple>): List<CommentBaseInfo> {
        return results.map { result ->
            mapCommentBaseInfoOfResult(result)
        }
    }

    @Transactional
    fun validateAndUpdateComment(command: CommentUpdateCommand): InternalCommentCommand {
        val comment = commentReaderStore.findCommentByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        comment.validateOwnerAndRequester(command.internalUserInfo)

        comment.updateContent(command.content)
        val updatedComment = commentReaderStore.saveComment(comment)
        return commentMapper.to(
            comment = updatedComment,
            userId = command.internalUserInfo.userId
        )
    }

    @Transactional
    fun validateAndUpdateStatus(command: CommentDeleteCommand) {
        val comment = commentReaderStore.findCommentByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        comment.validateOwnerAndRequester(command.internalUserInfo)
        comment.deleteComment()
    }

    fun findComment(command: SingleCommandSearchCommand): Tuple? {
        return commentReaderStore.findCommentRequest(command)
    }

    fun constructCommentBaseInfo(result: Tuple): CommentBaseInfo {
        return mapCommentBaseInfoOfResult(result)
    }

    fun constructInitialCommentBaseInfo(command: InternalCommentCommand): CommentBaseInfo {
        return CommentBaseInfo(
            commentUuid = command.uuid,
            content = command.content,
            userId = command.userId ?: "",
            likeCount = 0,
            replyCount = 0,
            createdDate = command.createdDate.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = command.modifiedCnt > 0,
            userLiked = false,
        )
    }

    fun createNewComment(command: CommentPostCommand): InternalCommentCommand {
        val user = userReaderStore.findUserByUuid(command.internalUserInfo.uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val video = videoReaderStore.findVideoByTypeAndId(command.videoType, command.videoId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val comment = Comment(
            content = command.content,
            video = video,
            user = user,
        )
        val savedComment= commentReaderStore.saveComment(comment)
        return commentMapper.to(
            comment = savedComment,
            userId = command.internalUserInfo.userId,
        )
    }

    fun findComment(uuid: String): InternalCommentCommand {
        val comment = commentReaderStore.findCommentByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return commentMapper.to(comment)
    }

    fun searchAndLikeOrCreateNew(
        internalUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentCommand,
    ) {
        val commentLike = likeReaderStore.findCommentLikeRequest(internalCommentCommand.id, internalUserCommand.id)

        if(commentLike == null) {
            val command = CommentLikePostCommand(
                userId = internalUserCommand.id,
                commentId = internalCommentCommand.id,
            )
            likeReaderStore.saveCommentLikeRequest(command)
        } else {
            commentLike.like()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    fun searchAndUnlike(
        internalUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentCommand,
    ) {
        val commentLike = likeReaderStore.findCommentLike(internalCommentCommand.id, internalUserCommand.id)

        if(commentLike == null) {
            throw BaseException(ErrorCode.BAD_REQUEST, "You cannot unlike this comment")
        } else {
            commentLike.unlike()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }
}