package com.example.myvopiserver.domain.command

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.ReportType
import com.commoncoremodule.enums.VerifyStatus
import com.commoncoremodule.enums.VideoType
import java.time.LocalDateTime

data class CommentUpdateCommand(
    val internalUserCommand: InternalUserCommand,
    val content: String,
    val commentUuid: String,
    val videoType: VideoType,
    val videoId: String,
)

data class InternalCommentWithUserAndVideoCommand(
    val internalCommentCommand: InternalCommentCommand,
    val internalCommentOwnerCommand: InternalUserCommand,
    val internalVideoCommand: InternalVideoCommand,
    val internalVideoOwnerCommand: InternalUserCommand,
)

data class CommentPostCommand(
    val internalUserCommand: InternalUserCommand,
    val content: String,
    val videoType: VideoType,
    val videoId: String,
)

data class CommentDeleteCommand(
    val internalUserCommand: InternalUserCommand,
    val commentUuid: String,
    val videoType: VideoType,
    val videoId: String,
)

data class CommentLikeCommand(
    val internalUserCommand: InternalUserCommand,
    val commentUuid: String,
    val videoType: VideoType,
    val videoId: String,
)

data class CommentReportCommand(
    val internalUserCommand: InternalUserCommand,
    val commentUuid: String,
    val reportType: ReportType,
)

data class InternalCommentCommand(
    val id: Long,
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: CommentStatus,
    val userId: String?,
    val createdDate: LocalDateTime,
    val verificationStatus: VerifyStatus,
)

data class InternalCommentAndOwnerCommand(
    val internalCommentCommand: InternalCommentCommand,
    val commentOwnerCommand: InternalUserCommand,
)