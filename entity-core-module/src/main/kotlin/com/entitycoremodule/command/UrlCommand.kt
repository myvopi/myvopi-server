package com.entitycoremodule.command

import com.commoncoremodule.enums.VideoType

data class UrlCommand (
    val videoType: VideoType,
    val videoId: String,
)