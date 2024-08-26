package com.example.adminmyvopiserver.domain.info

data class ReplyBaseJpaInfo (
    val uuid: String,
    val content: String,
    val modifiedCnt: Int,
    val status: String,
    val verificationStatus: String,
    val videoId: String?,
    val userId: String?,
    val likeCount: Long?,
    val createdDate: String?,
    val modified: Int,
)