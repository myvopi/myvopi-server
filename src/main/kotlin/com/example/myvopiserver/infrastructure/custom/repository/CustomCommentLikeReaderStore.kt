package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand

interface CustomCommentLikeReaderStore {

    fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike?

    fun saveCommentLikeRequest(command: CommentLikePostCommand)
}