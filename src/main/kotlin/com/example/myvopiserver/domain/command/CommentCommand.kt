package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType
import java.time.LocalDateTime

data class CommentSearchFromVideoCommand(
    val internalUserCommand: InternalUserCommand?,
    val filter: SearchFilter,
    val reqPage: Int,
    val videoId: Long,
    val videoType: VideoType,
)

data class CommentSearchFromCommentCommand(
    val internalUserCommand: InternalUserCommand?,
    val filter: SearchFilter,
    val reqPage: Int,
    val videoId: String,
    val videoType: VideoType,
)

data class CommentUpdateCommand(
    val internalUserCommand: InternalUserCommand,
    val content: String,
    val commentUuid: String,
    val videoType: VideoType,
    val videoId: String,
)

data class CommentUpdateRequestCommand(
    val internalUserCommand: InternalUserCommand,
    val videoId: String,
    val videoType: VideoType,
    val commentUuid: String,
    val status: CommentStatus,
)

data class SingleCommentSearchCommand(
    val internalUserCommand: InternalUserCommand,
    val videoId: String,
    val videoType: VideoType,
    val commentUuid: String,
)

data class InternalCommentCommand(
    val id: Long,
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: CommentStatus,
    val userId: String?,
    val createdDate: LocalDateTime,
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

data class CommentLikePostCommand(
    val userId: Long,
    val commentId: Long,
)