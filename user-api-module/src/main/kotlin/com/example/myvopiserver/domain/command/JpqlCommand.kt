package com.example.myvopiserver.domain.command

data class ReplyLikePostRequestCommand(
    val userId: Long,
    val replyId: Long,
)

data class CommentLikePostRequestCommand(
    val userId: Long,
    val commentId: Long,
)