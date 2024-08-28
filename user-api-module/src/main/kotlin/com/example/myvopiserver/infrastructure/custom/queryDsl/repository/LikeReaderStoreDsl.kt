package com.example.myvopiserver.infrastructure.custom.queryDsl.repository

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.ReplyLike

interface LikeReaderStoreDsl {

    // Comment like
    fun findCommentLike(commentId: Long, userId: Long): CommentLike?

    // Reply like
    fun findReplyLike(replyId: Long, userId: Long): ReplyLike?
}