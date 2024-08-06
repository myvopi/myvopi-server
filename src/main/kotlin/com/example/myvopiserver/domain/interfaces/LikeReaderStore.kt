package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.Comment
import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.role.User

interface LikeReaderStore {
    // NOT USING
    fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike?

    fun findCommentLike(commentId: Long, userId: Long): CommentLike?

    fun saveCommentLikeRequest(command: CommentLikePostCommand)

    fun saveCommentLike(commentLike: CommentLike): CommentLike

    fun findCommentLikeByUserAndComment(user: User, comment: Comment): CommentLike?

    fun findReplyLikeByUserAndReply(user: User, reply: Reply): ReplyLike?

    fun saveReplyLike(replyLike: ReplyLike): ReplyLike
}