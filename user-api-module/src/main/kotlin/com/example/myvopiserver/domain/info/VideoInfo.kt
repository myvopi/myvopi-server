package com.example.myvopiserver.domain.info

data class VideoBaseInfo(
    val comments: List<CommentBaseInfo>,
    val message: String,
)