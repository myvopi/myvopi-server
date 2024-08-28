package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.Reply
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.command.ReplyLikePostCommand

interface LikeReaderStore {

    fun findCommentLikeDslRequest(commentId: Long, userId: Long): CommentLike?

    fun initialSaveCommentLikeJpqlRequest(command: CommentLikePostCommand)

    fun saveCommentLike(commentLike: CommentLike): CommentLike

    fun findReplyLikeDslRequest(replyId: Long, userId: Long): ReplyLike?

    fun findReplyLikeByUserAndReply(user: User, reply: Reply): ReplyLike?

    fun saveReplyLike(replyLike: ReplyLike): ReplyLike

    fun initialSaveReplyLikeJpqlRequest(command: ReplyLikePostCommand)
}