package com.example.myvopiserver.infrastructure.custom.queryDsl

import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.infrastructure.custom.queryDsl.alias.QEntityAlias
import com.example.myvopiserver.infrastructure.custom.queryDsl.repository.LikeReaderStoreDsl
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class LikeReaderStoreDslImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val qEntityAlias: QEntityAlias,
): LikeReaderStoreDsl {

    /**
     * select cl.*
     *   from comment_like cl
     *  where 1=1
     *    and cl.comment_id = 1
     *    and cl.user_id = 1
    * */
    override fun findCommentLike(commentId: Long, userId: Long): CommentLike? {
        val qCommentLike = qEntityAlias.qCommentLike
        return jpaQueryFactory
            .select(qCommentLike)
            .from(qCommentLike)
            .where(
                qCommentLike.comment.id.eq(commentId),
                qCommentLike.user.id.eq(userId),
            )
            .fetchOne()
    }

    override fun findReplyLike(replyId: Long, userId: Long): ReplyLike? {
        val qReplyLike = qEntityAlias.qReplyLike
        return jpaQueryFactory
            .select(qReplyLike)
            .from(qReplyLike)
            .where(
                qReplyLike.reply.id.eq(replyId),
                qReplyLike.user.id.eq(userId),
            )
            .fetchOne()
    }
}