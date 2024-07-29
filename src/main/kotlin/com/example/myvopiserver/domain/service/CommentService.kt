package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.interfaces.VideoReaderStore
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.infrastructure.custom.expression.AliasExpressions
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val alias: AliasExpressions,
    private val commentMapper: CommentMapper,
    private val userReaderStore: UserReaderStore,
    private val videoReaderStore: VideoReaderStore,
) {

    private fun mapCommentBaseInfoOfResult(result: Tuple): CommentBaseInfo {
        return CommentBaseInfo(
            commentUuid = result.get(alias.commentUuidAlias)!!,
            content = result.get(alias.commentContentAlias)!!,
            userId = result.get(alias.userIdAlias)!!,
            likeCount = result.get(alias.commentLikesCountAlias)!!,
            replyCount = result.get(alias.replyCountAlias)!!,
            createdDate = result.get(alias.createdDateTupleAlias)!!.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = result.get(alias.commentModifiedCntAlias)!! > 0,
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

    fun updateComment(command: CommentUpdateCommand): InternalCommentCommand {
        val comment = commentReaderStore.findCommentByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        comment.updateContent(command.content)
        val updatedComment = commentReaderStore.saveComment(comment)
        return commentMapper.to(
            comment = updatedComment,
            userId = command.internalUserInfo.userId
        )
    }

    fun updateStatus(command: CommentUpdateRequestCommand) {
        commentReaderStore.updateCommentStatusRequest(command)
    }

    fun findComment(command: SingleCommandSearchCommand): Tuple? {
        val result = commentReaderStore.findCommentRequest(command)
        return result
    }

    fun constructCommentBaseInfo(result: Tuple): CommentBaseInfo {
        return mapCommentBaseInfoOfResult(result)
    }

    fun constructCommentBaseInfo(command: InternalCommentCommand): CommentBaseInfo {
        return CommentBaseInfo(
            commentUuid = command.uuid,
            content = command.content,
            userId = command.userId,
            likeCount = 0,
            replyCount = 0,
            createdDate = command.createdDate.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = command.modifiedCnt > 0,
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
}