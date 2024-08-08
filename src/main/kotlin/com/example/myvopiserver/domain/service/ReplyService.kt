package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.config.exception.BaseException
import com.example.myvopiserver.common.config.exception.ErrorCode
import com.example.myvopiserver.common.config.exception.NotFoundException
import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.*
import com.example.myvopiserver.domain.command.*
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import com.example.myvopiserver.domain.interfaces.LikeReaderStore
import com.example.myvopiserver.domain.interfaces.ReplyReaderStore
import com.example.myvopiserver.domain.mapper.CommentMapper
import com.example.myvopiserver.domain.mapper.ReplyMapper
import com.example.myvopiserver.domain.mapper.UserMapper
import com.example.myvopiserver.domain.mapper.VideoMapper
import com.example.myvopiserver.domain.role.User
import com.example.myvopiserver.infrastructure.custom.alias.BasicAlias
import com.example.myvopiserver.infrastructure.custom.alias.QEntityAlias
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

    // Db-transactions
    fun createNewReply(
        postCommand: ReplyPostCommand,
        internalCommentCommand: InternalCommentWithUserAndVideoCommand,
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