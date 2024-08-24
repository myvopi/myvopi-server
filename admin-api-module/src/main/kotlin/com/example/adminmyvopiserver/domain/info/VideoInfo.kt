package com.example.adminmyvopiserver.domain.info

data class VideoBaseInfo(
    val comments: List<CommentBaseInfo>,
    val message: String,
)