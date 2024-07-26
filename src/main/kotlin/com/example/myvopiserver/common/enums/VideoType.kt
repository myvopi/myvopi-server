package com.example.myvopiserver.common.enums

import com.example.myvopiserver.common.util.EnumCodeCompanion

enum class VideoType(val def: String) {
    YT_VIDEO("ytv"),
    YT_SHORT("yts"),
    ;
    companion object: EnumCodeCompanion<VideoType, String>(VideoType.entries.associateBy(VideoType::def))
}