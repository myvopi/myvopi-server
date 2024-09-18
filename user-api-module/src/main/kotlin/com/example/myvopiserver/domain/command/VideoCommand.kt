package com.example.myvopiserver.domain.command

import com.commoncoremodule.enums.Preference
import com.commoncoremodule.enums.SearchFilter
import com.commoncoremodule.enums.VideoType

data class VideoSearchCommand(
    val internalUserCommand: InternalUserCommand?,
    val videoType: VideoType,
    val videoId: String,
    val filter: SearchFilter,
    val reqPage: Int,
    val preferences: Map<Preference, Any>,
)

data class InternalVideoCommandWithMessage(
    val internalVideoCommand: InternalVideoCommand,
    val message: String,
    val search: Boolean,
)

data class InternalVideoAndOwnerCommand(
    val internalVideoCommand: InternalVideoCommand,
    val internalUserCommand: InternalUserCommand,
)

data class InternalVideoCommand(
    val id: Long,
    val uuid: String,
    val videoId: String,
    val userId: Long,
    val videoType: VideoType,
)