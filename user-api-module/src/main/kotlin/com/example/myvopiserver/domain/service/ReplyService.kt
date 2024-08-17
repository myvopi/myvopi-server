package com.example.myvopiserver.domain.service

import com.commoncoremodule.enums.ContentType
import com.commoncoremodule.exception.BaseException
import com.commoncoremodule.exception.ErrorCode
import com.commoncoremodule.exception.NotFoundException
import com.commoncoremodule.extension.toStrings
import com.entitycoremodule.command.*
import com.entitycoremodule.domain.comment.Comment
import com.entitycoremodule.domain.interfaces.users.LikeReaderStore
import com.entitycoremodule.domain.reply.Reply
import com.entitycoremodule.domain.interfaces.users.ReplyReaderStore
import com.entitycoremodule.domain.interfaces.users.ReportReaderStore
import com.entitycoremodule.domain.report.Report
import com.entitycoremodule.domain.user.User
import com.entitycoremodule.domain.video.Video
import com.entitycoremodule.info.ReplyBaseInfo
import com.entitycoremodule.infrastructure.users.custom.alias.BasicAlias
import com.entitycoremodule.infrastructure.users.custom.alias.QEntityAlias
import com.entitycoremodule.mapper.comment.CommentMapper
import com.entitycoremodule.mapper.common.UserMapper
import com.entitycoremodule.mapper.reply.ReplyMapper
import com.entitycoremodule.mapper.video.VideoMapper
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class ReplyService(
    private val replyReaderStore: ReplyReaderStore,
    private val alias: BasicAlias,
    private val validationService: ValidationService,
    private val replyMapper: ReplyMapper,
    private val userMapper: UserMapper,
    private val videoMapper: VideoMapper,
    private val commentMapper: CommentMapper,
    private val likeReaderStore: LikeReaderStore,
    private val entityAlias: QEntityAlias,
    private val reportReaderStore: ReportReaderStore,
) {

    // Db-transactions (readOnly)
    fun findReplies(command: ReplySearchCommand): List<Tuple> {
        return replyReaderStore.findRepliesRequest(command)
    }

    fun findOnlyReply(uuid: String): InternalReplyCommand {
        val reply = replyReaderStore.findReplyByUuid(uuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        return replyMapper.to(reply = reply)
    }

    fun findReply(command: SingleReplySearchCommand): Tuple {
        return replyReaderStore.findReplyRequest(command)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
    }

    fun findReplyAndOwnerAndUpdateFlagged(uuid: String): InternalReplyAndOwnerCommand {
        val reply = replyReaderStore.findReplyWithUserByUuid(uuid)
            ?.let {
                if(!validationService.validateIfFlagged(it.status)) {
                    it.flagComment()
                    replyReaderStore.saveReply(it)
                } else it
            }
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val owner = reply.user
        return InternalReplyAndOwnerCommand(
            internalReplyCommand = replyMapper.to(reply),
            commentOwnerCommand = userMapper.to(owner)!!,
        )
    }

    // Db-transactions
    fun createNewReply(
        postCommand: ReplyPostCommand,
        internalCommentCommand: com.entitycoremodule.command.InternalCommentWithUserAndVideoCommand,
    ): InternalReplyCommand
    {
        val requester = User(command = postCommand.internalUserCommand)
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
        val reply = Reply(
            content = postCommand.content,
            comment = comment,
            user = requester,
        )
        val savedReply = replyReaderStore.saveReply(reply)
        return replyMapper.to(
            reply = savedReply,
            userId = postCommand.internalUserCommand.userId,
        )
    }

    fun searchAndUpdateLikeOrCreateNew(
        requesterUserCommand: InternalUserCommand,
        internalReplyCommand: InternalReplyCommand,
    ) {
        val replyLike = likeReaderStore.findReplyLikeRequest(internalReplyCommand.id, requesterUserCommand.id)
        if(replyLike == null) {
            val command = ReplyLikePostCommand(
                userId = requesterUserCommand.id,
                replyId = internalReplyCommand.id,
            )
            likeReaderStore.initialSaveReplyLikeRequest(command)
        } else {
            validationService.validateIsLiked(replyLike.status)
            replyLike.like()
            likeReaderStore.saveReplyLike(replyLike)
        }
    }

    fun searchAndUpdateUnlike(
        requesterUserCommand: InternalUserCommand,
        internalReplyCommand: InternalReplyCommand,
    ) {
        val replyLike = likeReaderStore.findReplyLikeRequest(internalReplyCommand.id, requesterUserCommand.id)
        if(replyLike == null) {
            throw BaseException(ErrorCode.BAD_REQUEST, "You haven't even liked this reply")
        } else {
            validationService.validateIsUnliked(replyLike.status)
            replyLike.unlike()
            likeReaderStore.saveReplyLike(replyLike)
        }
    }

    // Validation
    fun validateAndUpdateContent(command: ReplyUpdateCommand) {
        val reply = replyReaderStore.findReplyWithUserByUuid(command.replyUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val commentOwner = reply.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, commentOwner)
        validationService.validateIsDeleted(reply.status)
        if(!validationService.validateIfRequestContentMatchesOriginalContent(command.content, reply.content)) {
            reply.updateContent(command.content)
        }
        replyReaderStore.saveReply(reply)
    }

    fun validateAndUpdateStatus(command: ReplyDeleteCommand) {
        val reply = replyReaderStore.findReplyWithUserByUuid(command.replyUuid)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND)
        val replyOwner = reply.user
        validationService.validateOwnerAndRequester(command.internalUserCommand, replyOwner)
        validationService.validateIsDeleted(reply.status)
        reply.deleteComment()
        replyReaderStore.saveReply(reply)
    }

    fun validateReportOrStore(
        replyReportCommand: ReplyReportCommand,
        replyAndOwnerCommand: InternalReplyAndOwnerCommand,
    ) {
        val reporter = User(command = replyReportCommand.internalUserCommand)
        reportReaderStore.findCommentReportByContentUuidAndUser(replyReportCommand.replyUuid, reporter)
            ?: run {
                val reportTarget = User(replyAndOwnerCommand.commentOwnerCommand)
                val report = Report(
                    contentType = ContentType.REPLY,
                    reportType = replyReportCommand.reportType,
                    contentUuid = replyAndOwnerCommand.internalReplyCommand.uuid,
                    contentId = replyAndOwnerCommand.internalReplyCommand.id,
                    reporter = reporter,
                    reportTarget = reportTarget,
                )
                reportReaderStore.saveReport(report)
            }
    }

    // Private & constructors
    private fun mapReplyBaseInfoOfResult(result: Tuple): ReplyBaseInfo {
        return ReplyBaseInfo(
            uuid = result.get(alias.columnReplyUuid)!!,
            content = result.get(alias.columnReplyContent)!!,
            userId = result.get(alias.columnUserId)!!,
            replyLikeCount = result.get(alias.columnReplyLikesCount)!!,
            modified = result.get(alias.columnReplyModifiedCnt)!! > 0,
            createdDate = result.get(alias.columnCreatedDateTuple)!!.toStrings("yyyy-MM-dd HH:mm:ss"),
            userLiked = result.get(alias.columnUserLiked)?: false,
        )
    }

    fun constructReplyBaseInfo(results: List<Tuple>): List<ReplyBaseInfo> {
        return results.map { result ->
            mapReplyBaseInfoOfResult(result)
        }
    }

    fun constructReplyBaseInfo(result: Tuple): ReplyBaseInfo {
        return mapReplyBaseInfoOfResult(result)
    }

    fun constructInitialReplyBaseInfo(command: InternalReplyCommand): ReplyBaseInfo {
        return ReplyBaseInfo(
            uuid = command.uuid,
            content = command.content,
            userId = command.userId ?: "",
            replyLikeCount = 0,
            createdDate = command.createdDate.toStrings("yyyy-MM-dd HH:mm:ss"),
            modified = command.modifiedCnt > 0,
            userLiked = false,
        )
    }

    private fun extractResultToCommand(tuple: Tuple): InternalReplyWithUserCommentAndVideoCommand {
        val reply = tuple.get(entityAlias.qReply) as Reply
        val comment = tuple.get(entityAlias.qComment) as Comment
        val video = tuple.get(entityAlias.qVideo) as Video
        val replyOwner = tuple.get(entityAlias.qReplyUser) as User
        val commentOwner = tuple.get(entityAlias.qCommentUser) as User
        val videoOwner = tuple.get(entityAlias.qVideoUser) as User
        return InternalReplyWithUserCommentAndVideoCommand(
            internalReplyCommand = replyMapper.to(reply),
            internalReplyOwnerCommand = userMapper.to(replyOwner)!!,
            internalCommentCommand = commentMapper.to(comment),
            internalCommentOwnerCommand = userMapper.to(commentOwner)!!,
            internalVideoCommand = videoMapper.to(video),
            internalVideoOwnerCommand = userMapper.to(videoOwner)!!,
        )
    }

    fun constructSingleReplySearchCommand(
        command: ReplyUpdateCommand
    ): SingleReplySearchCommand
    {
        return SingleReplySearchCommand(
            internalUserCommand = command.internalUserCommand,
            replyUuid = command.replyUuid,
        )
    }
}