package com.example.myvopiserver.domain.service

import com.commoncoremodule.exception.BadRequestException
import com.commoncoremodule.exception.BaseException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.ContentType
import com.commoncoremodule.extension.toStrings
import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.Report
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.Video
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.domain.interfaces.ReportReaderStore
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.mapper.VideoMapper
import com.example.myvopiserver.infrastructure.custom.queryDsl.alias.BasicAlias
import org.springframework.stereotype.Service
import com.querydsl.core.Tuple
import org.springframework.cache.annotation.Cacheable

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val alias: BasicAlias,
    private val likeReaderStore: LikeReaderStore,
    private val validationService: ValidationService,
    private val commentMapper: CommentMapper,
    private val videoMapper: VideoMapper,
    private val userMapper: UserMapper,
    private val reportReaderStore: ReportReaderStore,
) {

    // Db-transactions (readOnly)
    fun getCommentsRequest(command: CommentsSearchCommand): List<Tuple> {
        return commentReaderStore.findCommentsDslRequest(command)
    }

    fun getComment(command: SingleCommentSearchCommand): Tuple {
        return commentReaderStore.findCommentDslRequest(command)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
    }

    @Cacheable(value = ["cacheStore"], key = "#uuid")
    fun getOnlyComment(uuid: String): InternalCommentCommand {
        val comment = commentReaderStore.findCommentByUuid(uuid)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
        return commentMapper.to(comment = comment)
    }

    fun getCommentAndOwnerAndUpdateFlagged(uuid: String): InternalCommentAndOwnerCommand {
        val comment = commentReaderStore.findCommentWithUserByUuid(uuid)
            ?.let {
                // 삭제 여부 확인
                validationService.validateIsDeleted(it.status)
                // 신고 되었었는지 확인
                if(it.status == CommentStatus.FLAGGED) {
                    it.flagComment()
                    commentReaderStore.saveComment(it)
                } else it
            }
            ?: run { throw NotFoundException(ErrorCode.NOT_FOUND) }
        val owner = comment.user
        return InternalCommentAndOwnerCommand(
            internalCommentCommand = commentMapper.to(comment),
            commentOwnerCommand = userMapper.to(owner)!!,
        )
    }

    fun getCommentRelations(uuid: String): InternalCommentWithUserAndVideoCommand {
        val comment = commentReaderStore.findCommentWithUserAndVideoAndVideoOwnerByUuid(uuid)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
        return extractEntityToCommand(comment)
    }

    // Db-transactions
    fun createNewComment(
        postCommand: CommentPostCommand,
        internalVideoCommand: InternalVideoAndOwnerCommand,
    ): InternalCommentCommand {
        val videoOwner = User(command = internalVideoCommand.internalUserCommand)
        val requesterCommand = postCommand.internalUserCommand
        val requester = User(requesterCommand)
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
            userId = requesterCommand.userId ?: requesterCommand.displayUuid,
        )
    }

    fun getAndUpdateLikeOrCreateNew(
        requesterUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentCommand,
    ) {
        validationService.validateIsDeleted(internalCommentCommand.status)
        val commentLike = likeReaderStore.findCommentLikeDslRequest(internalCommentCommand.id, requesterUserCommand.id)
        if(commentLike == null) {
            val command = CommentLikePostRequestCommand(
                userId = requesterUserCommand.id,
                commentId = internalCommentCommand.id,
            )
            likeReaderStore.initialSaveCommentLikeJpqlRequest(command)
        } else {
            validationService.validateIsLiked(commentLike.status)
            commentLike.like()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    fun getAndUpdateUnlike(
        requesterUserCommand: InternalUserCommand,
        internalCommentCommand: InternalCommentCommand,
    ) {
        validationService.validateIsDeleted(internalCommentCommand.status)
        val commentLike = likeReaderStore.findCommentLikeDslRequest(internalCommentCommand.id, requesterUserCommand.id)
        if(commentLike == null) {
            throw BaseException(ErrorCode.BAD_REQUEST, "You haven't even liked this comment")
        } else {
            validationService.validateIsUnliked(commentLike.status)
            commentLike.unlike()
            likeReaderStore.saveCommentLike(commentLike)
        }
    }

    // Validation
    fun validateAndUpdateContent(command: CommentUpdateCommand): InternalCommentCommand {
        val comment = commentReaderStore.findCommentWithUserByUuid(command.commentUuid)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
        val commentOwner = comment.user
        // validate if is owner
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        // validate if it's deleted
        validationService.validateIsDeleted(comment.status)
        comment.updateContent(command.content)
        val savedComment = commentReaderStore.saveComment(comment)
        return commentMapper.to(savedComment)
    }

    fun validateAndDelete(command: CommentDeleteCommand) {
        val comment = commentReaderStore.findCommentWithUserByUuid(command.commentUuid)
            ?: throw BadRequestException(ErrorCode.NOT_FOUND)
        val commentOwner = comment.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        validationService.validateIsDeleted(comment.status)
        comment.deleteComment()
        commentReaderStore.saveComment(comment)
    }

    fun validateReportOrStore(
        commentReportCommand: CommentReportCommand,
        commentAndOwnerCommand: InternalCommentAndOwnerCommand,
    ) {
        val reporter = User(command = commentReportCommand.internalUserCommand)
        reportReaderStore.findCommentReportByTargetUuidAndUser(commentReportCommand.commentUuid, reporter)
            ?: run {
                val reportTarget = User(commentAndOwnerCommand.commentOwnerCommand)
                val report = Report(
                    contentType = ContentType.COMMENT,
                    reportType = commentReportCommand.reportType,
                    targetUuid = commentAndOwnerCommand.internalCommentCommand.uuid,
                    targetId = commentAndOwnerCommand.internalCommentCommand.id,
                    reporter = reporter,
                    reportTarget = reportTarget,
                )
                reportReaderStore.saveReport(report)
            }
    }

    // Private & constructors
    private fun mapCommentBaseInfoOfResult(result: Tuple): CommentBaseInfo {
        val userId = result.get(alias.columnUserId)
        val userDisplayUuid = result.get(alias.columnUserDisplayUuid)
        return CommentBaseInfo(
            uuid = result.get(alias.columnCommentUuid)!!,
            content = result.get(alias.columnCommentContent)!!,
            userId = if(!userId.isNullOrBlank()) userId else userDisplayUuid!!,
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
            userId = command.userId,
            likeCount = 0,
            replyCount = 0,
            createdDate = command.createdDate.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = command.modifiedCnt > 0,
            userLiked = false,
        )
    }

    fun constructSingleCommentSearchCommand(
        command: CommentUpdateCommand,
        internalCommentCommand: InternalCommentCommand,
    ): SingleCommentSearchCommand {
        return SingleCommentSearchCommand(
            internalUserCommand = command.internalUserCommand,
            videoId = command.videoId,
            videoType = command.videoType,
            commentUuid = command.commentUuid,
            commentId = internalCommentCommand.id,
        )
    }

    fun constructCommentSearchCommandForVideoRequest(
        command: VideoSearchCommand
    ): CommentsSearchCommand {
        return CommentsSearchCommand(
            filter = command.filter,
            reqPage = command.reqPage,
            videoId = command.videoId,
            videoType = command.videoType,
            internalUserCommand = command.internalUserCommand,
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