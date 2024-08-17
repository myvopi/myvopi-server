package com.entitycoremodule.infrastructure.users.custom.repository

import com.entitycoremodule.command.CommentLikePostCommand
import com.entitycoremodule.command.ReplyLikePostCommand
import com.entitycoremodule.domain.like.CommentLike
import com.entitycoremodule.domain.like.ReplyLike

interface CustomLikeReaderStore {

    // Comment like
    fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike?

    fun saveCommentLikeRequest(command: CommentLikePostCommand)

    // Reply like
    fun findReplyLikeRequest(replyId: Long, userId: Long): ReplyLike?

    fun saveReplyLikeRequest(command: ReplyLikePostCommand)
}