package com.example.myvopiserver.domain.command

import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.common.enums.VideoType

data class CommentSearchFromVideoCommand(
    val filter: SearchFilter,
    val reqPage: Int,
    val videoId: Long,
    // TODO user id
)

data class CommentSearchFromCommentCommand(
    val filter: SearchFilter,
    val reqPage: Int,
    val videoId: String,
    val videoType: VideoType,
)