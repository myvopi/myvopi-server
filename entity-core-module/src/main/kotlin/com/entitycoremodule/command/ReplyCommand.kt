package com.entitycoremodule.command

import com.commoncoremodule.enums.ReportType

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