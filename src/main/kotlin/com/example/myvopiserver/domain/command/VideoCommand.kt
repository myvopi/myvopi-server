package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType

data class VideoSearchCommand(
    val authenticationState: Boolean,
    val internalUserCommand: InternalUserCommand?,
    val videoType: VideoType,
    val videoId: String,
    val filter: SearchFilter,
    val reqPage: Int,
)

data class InternalVideoCommand(
    val id: Long,
    val uuid: String,
    val videoId: String,
    val userId: Long,
    val videoType: VideoType,
)
