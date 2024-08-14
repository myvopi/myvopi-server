package com.entitycoremodule.command

data class CommentLikePostCommand(
    val userId: Long,
    val commentId: Long,
)

data class ReplyLikePostCommand(
    val userId: Long,
    val replyId: Long,
)