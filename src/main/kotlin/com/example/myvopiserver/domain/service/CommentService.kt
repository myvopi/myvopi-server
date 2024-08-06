package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.BaseException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.mapper.VideoMapper
import com.example.myvopiserver.domain.role.User
import com.example.myvopiserver.infrastructure.custom.alias.BasicAlias
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val alias: BasicAlias,
    private val likeReaderStore: LikeReaderStore,
    private val validationService: ValidationService,
    private val commentMapper: CommentMapper,
    private val videoMapper: VideoMapper,
    private val userMapper: UserMapper,
) {

    // Db-transactions (readOnly)
    fun findCommentsFromVideo(command: CommentSearchFromVideoCommand): List<Tuple> {
        return commentReaderStore.findCommentsFromVideoRequest(command)
    }

    fun findComments(command: CommentSearchFromCommentCommand): List<Tuple> {
        return commentReaderStore.findCommentsFromCommentRequest(command)
    }

    fun findComment(command: SingleCommentSearchCommand): Tuple {
        return commentReaderStore.findCommentRequest(command)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
    }

    fun findCommentWithUserAndVideo(uuid: String): InternalCommentWithUserAndVideoCommand {
        val comment = commentReaderStore.findCommentWithUserAndVideoAndVideoOwnerByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return extractEntityToCommand(comment)
    }

    fun findComment(uuid: String): InternalCommentWithUserAndVideoCommand {
        val comment = commentReaderStore.findCommentByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return extractEntityToCommand(comment)
    }

    // Db-transactions
    fun createNewComment(
        postCommand: CommentPostCommand,
        internalVideoCommand: InternalVideoAndOwnerCommand,
    ): InternalCommentCommand {
        val videoOwner = User(command = internalVideoCommand.internalUserCommand)
        val requester = User(postCommand.internalUserCommand)
        val video = Video(
            command = internalVideoCommand.internalVideoCommand,
            user = videoOwner,
        )
        val comment = Comment(
            content = postCommand.content,
            video = video,
            user = requester,
        )
        val savedComment= commentReaderStore.saveComment(comment)
        return commentMapper.to(
            comment = savedComment,
            userId = postCommand.internalUserCommand.userId,
        )
    }

    fun searchAndUpdateLikeOrCreateNew(
        requestedInternalUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentWithUserAndVideoCommand,
    ) {
        val requester = User(command = requestedInternalUserCommand)
        val commentOwner = User(command = internalCommentCommand.internalCommentOwnerCommand)
        val videoOwner = User(command = internalCommentCommand.internalVideoOwnerCommand)
        val video = Video(
            command = internalCommentCommand.internalVideoCommand,
            user = videoOwner,
        )
        val comment = Comment(
            command = internalCommentCommand.internalCommentCommand,
            user = commentOwner,
            video = video,
        )
        val commentLike = likeReaderStore.findCommentLikeByUserAndComment(requester, comment)
        if(commentLike == null) {
            val newCommentLike = CommentLike(
                comment = comment,
                user = requester,
            )
            likeReaderStore.saveCommentLike(newCommentLike)
        } else {
            commentLike.like()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    fun searchAndUpdateUnlike(
        requestedInternalUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentWithUserAndVideoCommand,
    ) {
        val requester = User(command = requestedInternalUserCommand)
        val commentOwner = User(command = internalCommentCommand.internalCommentOwnerCommand)
        val videoOwner = User(command = internalCommentCommand.internalVideoOwnerCommand)
        val video = Video(
            command = internalCommentCommand.internalVideoCommand,
            user = videoOwner,
        )
        val comment = Comment(
            command = internalCommentCommand.internalCommentCommand,
            user = commentOwner,
            video = video,
        )
        val commentLike = likeReaderStore.findCommentLikeByUserAndComment(requester, comment)
        if(commentLike == null) {
            throw BaseException(ErrorCode.BAD_REQUEST, "You haven't even liked this comment")
        } else {
            commentLike.unlike()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    // Validation
    fun validateAndUpdateContent(command: CommentUpdateCommand) {
        val comment = commentReaderStore.findCommentWithUserByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val commentOwner = comment.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        comment.updateContent(command.content)
        commentReaderStore.saveComment(comment)
    }

    fun validateAndUpdateStatus(command: CommentDeleteCommand) {
        val comment = commentReaderStore.findCommentWithUserByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val commentOwner = comment.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        comment.deleteComment()
        commentReaderStore.saveComment(comment)
    }

    // Private & constructors
    private fun mapCommentBaseInfoOfResult(result: Tuple): CommentBaseInfo {
        return CommentBaseInfo(
            uuid = result.get(alias.columnCommentUuid)!!,
            content = result.get(alias.columnCommentContent)!!,
            userId = result.get(alias.columnUserId)!!,
            likeCount = result.get(alias.columnCommentLikesCount)!!,
            replyCount = result.get(alias.columnReplyCount)!!,
            createdDate = result.get(alias.columnCreatedDateTuple)!!.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = result.get(alias.columnCommentModifiedCnt)!! > 0,
            userLiked = result.get(alias.columnUserLiked)?: false,
        )
    }

    fun constructCommentBaseInfo(results: List<Tuple>): List<CommentBaseInfo> {
        return results.map { result ->
            mapCommentBaseInfoOfResult(result)
        }
    }

    fun constructCommentBaseInfo(result: Tuple): CommentBaseInfo {
        return mapCommentBaseInfoOfResult(result)
    }

    fun constructInitialCommentBaseInfo(command: InternalCommentCommand): CommentBaseInfo {
        return CommentBaseInfo(
            uuid = command.uuid,
            content = command.content,
            userId = command.userId ?: "",
            likeCount = 0,
            replyCount = 0,
            createdDate = command.createdDate.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = command.modifiedCnt > 0,
            userLiked = false,
        )
    }

    fun constructSingleCommentSearchCommand(
        command: CommentUpdateCommand,
    ): SingleCommentSearchCommand {
        return SingleCommentSearchCommand(
            internalUserCommand = command.internalUserCommand,
            videoId = command.videoId,
            videoType = command.videoType,
            commentUuid = command.commentUuid,
        )
    }

    private fun extractEntityToCommand(comment: Comment): InternalCommentWithUserAndVideoCommand {
        val commentOwner = comment.user
        val video = comment.video
        val videoOwner = video.user
        return InternalCommentWithUserAndVideoCommand(
            internalCommentCommand = commentMapper.to(comment = comment),
            internalCommentOwnerCommand = userMapper.to(user = commentOwner)!!,
            internalVideoCommand = videoMapper.to(video = video),
            internalVideoOwnerCommand = userMapper.to(user = videoOwner)!!,
        )
    }
}