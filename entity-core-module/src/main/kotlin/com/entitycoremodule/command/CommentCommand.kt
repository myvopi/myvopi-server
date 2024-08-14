package com.entitycoremodule.command

import com.commoncoremodule.enums.VideoType

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