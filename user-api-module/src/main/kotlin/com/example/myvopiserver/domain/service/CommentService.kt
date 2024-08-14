package com.example.myvopiserver.domain.service

import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.BaseException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.extension.toStrings
import com.entitycoremodule.command.*
import com.entitycoremodule.domain.comment.Comment
import com.entitycoremodule.domain.comment.CommentReaderStore
import com.entitycoremodule.domain.like.LikeReaderStore
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.domain.video.Video
import com.entitycoremodule.info.CommentBaseInfo
import com.entitycoremodule.infrastructure.custom.alias.BasicAlias
import com.entitycoremodule.mapper.comment.CommentMapper
import com.entitycoremodule.mapper.common.UserMapper
import com.entitycoremodule.mapper.video.VideoMapper
import org.springframework.stereotype.Service
import com.querydsl.core.Tuple

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

    fun findOnlyComment(uuid: String): InternalCommentCommand {
        val comment = commentReaderStore.findCommentByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return commentMapper.to(comment = comment)
    }

    fun findCommentRelations(uuid: String): com.entitycoremodule.command.InternalCommentWithUserAndVideoCommand {
        val comment = commentReaderStore.findCommentWithUserAndVideoAndVideoOwnerByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return extractEntityToCommand(comment)
    }

    // Db-transactions
    fun createNewComment(
        postCommand: com.entitycoremodule.command.CommentPostCommand,
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
        requesterUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentCommand,
    ) {
        validateStatus(internalCommentCommand)
        val commentLike = likeReaderStore.findCommentLikeRequest(internalCommentCommand.id, requesterUserCommand.id)
        if(commentLike == null) {
            val command = CommentLikePostCommand(
                userId = requesterUserCommand.id,
                commentId = internalCommentCommand.id,
            )
            likeReaderStore.initialSaveCommentLikeRequest(command)
        } else {
            validationService.validateIsLiked(commentLike.status)
            commentLike.like()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    fun searchAndUpdateUnlike(
        requesterUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentCommand,
    ) {
        validateStatus(internalCommentCommand)
        val commentLike = likeReaderStore.findCommentLikeRequest(internalCommentCommand.id, requesterUserCommand.id)
        if(commentLike == null) {
            throw BaseException(ErrorCode.BAD_REQUEST, "You haven't even liked this comment")
        } else {
            validationService.validateIsUnliked(commentLike.status)
            commentLike.unlike()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    // Validation
    fun validateAndUpdateContent(command: com.entitycoremodule.command.CommentUpdateCommand) {
        val comment = commentReaderStore.findCommentWithUserByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val commentOwner = comment.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        validationService.validateIsDeleted(comment.status)
        if(!validationService.validateIfRequestContentMatchesOriginalContent(command.content, comment.content)) {
            comment.updateContent(command.content)
        }
        commentReaderStore.saveComment(comment)
    }

    fun validateAndUpdateStatus(command: com.entitycoremodule.command.CommentDeleteCommand) {
        val comment = commentReaderStore.findCommentWithUserByUuid(command.commentUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val commentOwner = comment.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        validationService.validateIsDeleted(comment.status)
        comment.deleteComment()
        commentReaderStore.saveComment(comment)
    }

    fun validateStatus(command: InternalCommentCommand) {
        if(command.status == CommentStatus.DELETED) throw BadRequestException(ErrorCode.BAD_REQUEST, "This comment has already been deleted")
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
        command: com.entitycoremodule.command.CommentUpdateCommand,
    ): SingleCommentSearchCommand {
        return SingleCommentSearchCommand(
            internalUserCommand = command.internalUserCommand,
            videoId = command.videoId,
            videoType = command.videoType,
            commentUuid = command.commentUuid,
        )
    }

    private fun extractEntityToCommand(comment: Comment): com.entitycoremodule.command.InternalCommentWithUserAndVideoCommand {
        val commentOwner = comment.user
        val video = comment.video
        val videoOwner = video.user
        return com.entitycoremodule.command.InternalCommentWithUserAndVideoCommand(
            internalCommentCommand = commentMapper.to(comment = comment),
            internalCommentOwnerCommand = userMapper.to(user = commentOwner)!!,
            internalVideoCommand = videoMapper.to(video = video),
            internalVideoOwnerCommand = userMapper.to(user = videoOwner)!!,
        )
    }
}