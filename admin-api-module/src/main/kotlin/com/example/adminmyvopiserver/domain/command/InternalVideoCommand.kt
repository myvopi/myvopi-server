package com.example.adminmyvopiserver.domain.command

import com.commoncoremodule.enums.VideoType

data class InternalVideoCommand(
    val id: Long,
    val uuid: String,
    val videoId: String,
    val userId: Long,
    val videoType: VideoType,
)
