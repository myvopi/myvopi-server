package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.command.ReplyLikePostCommand
import com.example.myvopiserver.domain.role.User

interface LikeReaderStore {

    // Comment like
    fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike?

    fun initialSaveCommentLikeRequest(command: CommentLikePostCommand)

    fun saveCommentLike(commentLike: CommentLike): CommentLike

    // Reply like
    fun findReplyLikeRequest(replyId: Long, userId: Long): ReplyLike?

    fun findReplyLikeByUserAndReply(user: User, reply: Reply): ReplyLike?

    fun saveReplyLike(replyLike: ReplyLike): ReplyLike

    fun initialSaveReplyLikeRequest(command: ReplyLikePostCommand)
}