package com.example.myvopiserver.infrastructure.custom.queryDsl.constructor

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.LikeStatus
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.infrastructure.custom.queryDsl.alias.BasicAlias
import com.example.myvopiserver.infrastructure.custom.queryDsl.alias.QEntityAlias
import com.example.myvopiserver.infrastructure.custom.queryDsl.expression.CommentQueryExpressions
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.sql.JPASQLQuery
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class QueryDslConstructor(
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
    private val expressions: CommentQueryExpressions,
    private val jpaSqlQuery: JPASQLQuery<Any>,
) {

    fun verifyAuthAndConstructCommentSelectQuery(command: InternalUserCommand?): JPASQLQuery<Tuple> {
        return constructNonAuthCommentSelectQuery().apply {
                command?.let {
                    expressions.commentLikeSubQuery(command.id).`as`(alias.columnUserLiked)
                }
            }
    }

    fun constructNonAuthCommentSelectQuery(): JPASQLQuery<Tuple> {
        return jpaSqlQuery.select(
            qEntityAlias.qComment.uuid.`as`(alias.columnCommentUuid),
            qEntityAlias.qComment.content.`as`(alias.columnCommentContent),
            qEntityAlias.qComment.modifiedCnt.`as`(alias.columnCommentModifiedCnt),
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryCommentLike, qEntityAlias.qCommentLike.id.metadata.name).countDistinct().`as`(alias.columnCommentLikesCount), // likeCount
            Expressions.numberPath(Long::class.java, alias.subQueryReply, qEntityAlias.qReply.id.metadata.name).countDistinct().`as`(alias.columnReplyCount), // replyCount
            Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qComment, qEntityAlias.qComment.createdDt.metadata.name).`as`(alias.columnCreatedDate), // created_dt
        )
    }

    fun constructReplySubQuery(): JPQLQuery<Tuple> {
        return JPAExpressions
            .select(
                Expressions.numberPath(Long::class.java, qEntityAlias.qReply2, "comment_id").`as`("comment_id"),
                qEntityAlias.qReply2.id
            )
            .from(qEntityAlias.qReply2)
            .where(
                Expressions.stringPath(qEntityAlias.qReply2, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name)
            )
    }

    fun constructFilteredCommentLikeSubQuery(): JPQLQuery<Tuple> {
        return JPAExpressions
            .select(
                qEntityAlias.qCommentLike.id,
                Expressions.numberPath(Long::class.java, qEntityAlias.qCommentLike, "comment_id").`as`("comment_id"),
            )
            .from(qEntityAlias.qCommentLike)
            .where(
                Expressions.stringPath(qEntityAlias.qCommentLike, "status").eq(LikeStatus.LIKED.name)
            )
    }

    fun verifyAuthAndConstructReplySelectQuery(command: InternalUserCommand?): JPASQLQuery<Tuple> {
        return constructNonAuthReplySelectQuery().apply {
            command?.let {
                expressions.replyLikeSubQuery(command.id).`as`(alias.columnUserLiked)
            }
        }
    }

    fun constructNonAuthReplySelectQuery(): JPASQLQuery<Tuple> {
        return jpaSqlQuery.select(
            qEntityAlias.qReply.uuid.`as`(alias.columnReplyUuid),
            qEntityAlias.qReply.content.`as`(alias.columnReplyContent),
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryReplyLike, qEntityAlias.qReplyLike.id.metadata.name).countDistinct().`as`(alias.columnReplyLikesCount), // reply likeCount
            qEntityAlias.qComment.modifiedCnt.`as`(alias.columnReplyModifiedCnt),
            Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qReply, qEntityAlias.qReply.createdDt.metadata.name).`as`(alias.columnCreatedDate), // created_dt
        )
    }

    fun constructFilteredReplyLikeSubQuery(): JPQLQuery<Tuple> {
        return JPAExpressions
            .select(
                qEntityAlias.qReplyLike2.id,
                Expressions.numberPath(Long::class.java, qEntityAlias.qReplyLike2, "reply_id").`as`("reply_id"),
            )
            .from(qEntityAlias.qReplyLike2)
            .where(
                Expressions.stringPath(qEntityAlias.qReplyLike2, qEntityAlias.qReplyLike2.status.metadata.name).eq(LikeStatus.LIKED.name)
            )
    }
}