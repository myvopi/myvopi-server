package com.example.myvopiserver.infrastructure.custom.expression

import com.commoncoremodule.enums.LikeStatus
import com.example.myvopiserver.infrastructure.custom.alias.QEntityAlias
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.stereotype.Component

@Component
class CommentQueryExpressions(
    private val qEntityAlias: QEntityAlias,
) {

    fun commentLikeSubQuery(userId: Long): BooleanExpression {
        return JPAExpressions
            .select(Expressions.TRUE)
            .from(qEntityAlias.qCommentLike2)
            .join(qEntityAlias.qComment2).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qCommentLike2, "comment_id").eq(qEntityAlias.qComment2.id))
            .where(
                Expressions.numberPath(Long::class.java, qEntityAlias.qComment2, "video_id")
                    .eq(Expressions.numberPath(Long::class.java, qEntityAlias.qComment, "video_id")),
                qEntityAlias.qComment2.id.eq(qEntityAlias.qComment.id),
                Expressions.numberPath(Long::class.java, qEntityAlias.qCommentLike2, "user_id").eq(userId),
                Expressions.stringPath(qEntityAlias.qCommentLike2, "like_status").eq(LikeStatus.LIKED.name),
            )
            .exists()
    }

    fun replyLikeSubQuery(userId: Long): BooleanExpression {
        return JPAExpressions
            .select(Expressions.TRUE)
            .from(qEntityAlias.qReplyLike2)
            .join(qEntityAlias.qReply2).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qReplyLike2, "reply_id").eq(qEntityAlias.qReply2.id))
            .where(
                qEntityAlias.qReply2.id.eq(qEntityAlias.qReply.id),
                Expressions.numberPath(Long::class.java, qEntityAlias.qReplyLike2, "user_id").eq(userId),
                Expressions.stringPath(qEntityAlias.qReplyLike2, "like_status").eq(LikeStatus.LIKED.name),
            )
            .exists()
    }
}