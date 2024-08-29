package com.example.myvopiserver.domain.command

import com.commoncoremodule.enums.SearchFilter
import com.commoncoremodule.enums.VideoType

data class CommentsSearchCommand(
    val internalUserCommand: InternalUserCommand?,
    val filter: SearchFilter,
    val reqPage: Int,
    val videoId: String,
    val videoType: VideoType,
)

data class SingleCommentSearchCommand(
    val internalUserCommand: InternalUserCommand,
    val videoId: String,
    val videoType: VideoType,
    val commentUuid: String,
)

data class ReplySearchCommand(
    val internalUserCommand: InternalUserCommand?,
    val commentUuid: String,
    val reqPage: Int,
)

data class SingleReplySearchCommand(
    val internalUserCommand: InternalUserCommand,
    val replyUuid: String,
)