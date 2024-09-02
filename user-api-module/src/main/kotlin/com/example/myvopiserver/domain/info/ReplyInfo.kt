package com.example.myvopiserver.domain.info

data class ReplyBaseInfo(
    val uuid: String,
    val content: String,
    val userId: String?,
    val likeCount: Long,
    val modified: Boolean,
    val createdDate: String,
    val userLiked: Boolean,
)