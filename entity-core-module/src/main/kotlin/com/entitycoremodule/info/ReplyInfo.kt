package com.entitycoremodule.info

data class ReplyBaseInfo (
    val uuid: String,
    val content: String,
    val userId: String,
    val replyLikeCount: Long,
    val modified: Boolean,
    val createdDate: String,
    val userLiked: Boolean,
)