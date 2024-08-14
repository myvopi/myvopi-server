package com.entitycoremodule.command

import com.commoncoremodule.enums.SearchFilter
import com.commoncoremodule.enums.VideoType

data class VideoSearchCommand(
    val internalUserCommand: InternalUserCommand?,
    val videoType: VideoType,
    val videoId: String,
    val filter: SearchFilter,
    val reqPage: Int,
)

data class InternalVideoCommandWithMessage(
    val internalVideoCommand: InternalVideoCommand,
    val message: String,
)

data class InternalVideoAndOwnerCommand(
    val internalVideoCommand: InternalVideoCommand,
    val internalUserCommand: InternalUserCommand,
)
