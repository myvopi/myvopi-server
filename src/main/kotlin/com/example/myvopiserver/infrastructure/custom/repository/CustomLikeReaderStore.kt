package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.command.ReplyLikePostCommand

interface CustomLikeReaderStore {

    // Comment like
    fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike?

    fun saveCommentLikeRequest(command: CommentLikePostCommand)

    // Reply like
    fun findReplyLikeRequest(replyId: Long, userId: Long): ReplyLike?

    fun saveReplyLikeRequest(command: ReplyLikePostCommand)
}