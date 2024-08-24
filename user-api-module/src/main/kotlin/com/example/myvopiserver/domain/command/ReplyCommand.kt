package com.example.myvopiserver.domain.command

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.ReportType
import com.commoncoremodule.enums.VerifyStatus
import java.time.LocalDateTime

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

data class InternalReplyWithUserCommentAndVideoCommand(
    val internalReplyCommand: InternalReplyCommand,
    val internalReplyOwnerCommand: InternalUserCommand,
    val internalCommentCommand: InternalCommentCommand,
    val internalCommentOwnerCommand: InternalUserCommand,
    val internalVideoCommand: InternalVideoCommand,
    val internalVideoOwnerCommand: InternalUserCommand,
)

data class ReplyReportCommand(
    val internalUserCommand: InternalUserCommand,
    val replyUuid: String,
    val reportType: ReportType,
)

data class InternalReplyCommand(
    val id: Long,
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: CommentStatus,
    val createdDate: LocalDateTime,
    val userId: String?,
    val verificationStatus: VerifyStatus,
)

data class InternalReplyAndOwnerCommand(
    val internalReplyCommand: InternalReplyCommand,
    val commentOwnerCommand: InternalUserCommand,
)