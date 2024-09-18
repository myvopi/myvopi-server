package com.example.myvopiserver.domain.info

data class CommentBaseInfo(
    val uuid: String,
    val content: String,
    val userId: String?,
    val likeCount: Long,
    val replyCount: Long,
    val createdDate: String,
    val modified: Boolean,
    val userLiked: Boolean,
)