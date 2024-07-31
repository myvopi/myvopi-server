package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand

interface LikeReaderStore {

    fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike?

    fun findCommentLike(commentId: Long, userId: Long): CommentLike?

    fun saveCommentLikeRequest(command: CommentLikePostCommand)

    fun saveCommentLike(commentLike: CommentLike): CommentLike
}