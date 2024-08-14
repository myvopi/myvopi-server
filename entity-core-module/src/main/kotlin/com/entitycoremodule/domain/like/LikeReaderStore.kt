package com.entitycoremodule.domain.like

import com.entitycoremodule.command.CommentLikePostCommand
import com.entitycoremodule.command.ReplyLikePostCommand
import com.entitycoremodule.domain.reply.Reply
import com.entitycoremodule.domain.user.User

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