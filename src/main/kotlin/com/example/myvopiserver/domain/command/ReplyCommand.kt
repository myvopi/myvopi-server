package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.CommentStatus
import java.time.LocalDateTime

data class ReplySearchCommand(
    val internalUserCommand: InternalUserCommand?,
    val commentUuid: String,
    val reqPage: Int,
)

data class SingleReplySearchCommand(
    val internalUserCommand: InternalUserCommand,
    val replyUuid: String,
)

data class ReplyUpdateCommand(
    val internalUserCommand: InternalUserCommand,
    val content: String,
    val replyUuid: String,
)

data class ReplyPostCommand(
    val internalUserCommand: InternalUserCommand,
    val content: String,
    val commentUuid: String,
)

data class ReplyDeleteCommand(
    val internalUserCommand: InternalUserCommand,
    val replyUuid: String,
)

data class ReplyLikeCommand(
    val internalUserCommand: InternalUserCommand,
    val replyUuid: String,
)

data class InternalReplyCommand(
    val id: Long,
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: CommentStatus,
    val createdDate: LocalDateTime,
    val userId: String?,
)

data class InternalReplyWithUserCommentAndVideoCommand(
    val internalReplyCommand: InternalReplyCommand,
    val internalReplyOwnerCommand: InternalUserCommand,
    val internalCommentCommand: InternalCommentCommand,
    val internalCommentOwnerCommand: InternalUserCommand,
    val internalVideoCommand: InternalVideoCommand,
    val internalVideoOwnerCommand: InternalUserCommand,
)