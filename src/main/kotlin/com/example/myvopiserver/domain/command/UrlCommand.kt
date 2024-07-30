package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.VideoType

data class UrlCommand (
    val videoType: VideoType,
    val videoId: String,
)