package com.example.myvopiserver.infrastructure.custom.queryDsl

import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.common.enums.LikeStatus
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.infrastructure.custom.alias.BasicAlias
import com.example.myvopiserver.infrastructure.custom.alias.QEntityAlias
import com.example.myvopiserver.infrastructure.custom.expression.CommentQueryExpressions
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.sql.JPASQLQuery
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CommentQueryConstructor(
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
    private val em: EntityManager,
    private val mysqlTemplates: SQLTemplates,
    private val expressions: CommentQueryExpressions,
) {

    fun verifyAuthAndConstructCommentSelectQuery(command: InternalUserCommand?): JPASQLQuery<Tuple> {
        return command?.let { constructAuthCommentSelectQuery(it) }
            ?: run { constructNonAuthCommentSelectQuery() }
    }

    fun constructAuthCommentSelectQuery(command: InternalUserCommand): JPASQLQuery<Tuple> {
        val query = JPASQLQuery<Any>(em, mysqlTemplates)
        return query.select(
            qEntityAlias.qComment.uuid.`as`(alias.columnCommentUuid),
            qEntityAlias.qComment.content.`as`(alias.columnCommentContent),
            qEntityAlias.qComment.modifiedCnt.`as`(alias.columnCommentModifiedCnt),
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryCommentLike, "id").countDistinct().`as`(alias.columnCommentLikesCount), // likeCount
            Expressions.numberPath(Long::class.java, alias.subQueryReply, "id").countDistinct().`as`(alias.columnReplyCount), // replyCount
            Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qComment, "created_dt").`as`(alias.columnCreatedDate), // created_dt
            expressions.commentLikeSubQuery(command.id).`as`(alias.columnUserLiked)
        )
    }

    fun constructNonAuthCommentSelectQuery(): JPASQLQuery<Tuple> {
        val query = JPASQLQuery<Any>(em, mysqlTemplates)
        return query.select(
            qEntityAlias.qComment.uuid.`as`(alias.columnCommentUuid),
            qEntityAlias.qComment.content.`as`(alias.columnCommentContent),
            qEntityAlias.qComment.modifiedCnt.`as`(alias.columnCommentModifiedCnt),
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryCommentLike, "id").countDistinct().`as`(alias.columnCommentLikesCount), // likeCount
            Expressions.numberPath(Long::class.java, alias.subQueryReply, "id").countDistinct().`as`(alias.columnReplyCount), // replyCount
            Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qComment, "created_dt").`as`(alias.columnCreatedDate), // created_dt
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
                Expressions.stringPath(qEntityAlias.qCommentLike, "like_status").eq(LikeStatus.LIKED.name)
            )
    }

    fun verifyAuthAndConstructReplySelectQuery(command: InternalUserCommand?): JPASQLQuery<Tuple> {
        return command?.let { constructAuthReplySelectQuery(it) }
            ?: run { constructNonAuthReplySelectQuery() }
    }

    fun constructAuthReplySelectQuery(command: InternalUserCommand): JPASQLQuery<Tuple> {
        val query = JPASQLQuery<Any>(em, mysqlTemplates)
        return query.select(
            qEntityAlias.qReply.uuid.`as`(alias.columnReplyUuid),
            qEntityAlias.qReply.content.`as`(alias.columnReplyContent),
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryReplyLike, "id").countDistinct().`as`(alias.columnReplyLikesCount), // reply likeCount
            qEntityAlias.qComment.modifiedCnt.`as`(alias.columnReplyModifiedCnt),
            Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qReply, "created_dt").`as`(alias.columnCreatedDate), // created_dt
            expressions.replyLikeSubQuery(command.id).`as`(alias.columnUserLiked)
        )
    }

    fun constructNonAuthReplySelectQuery(): JPASQLQuery<Tuple> {
        val query = JPASQLQuery<Any>(em, mysqlTemplates)
        return query.select(
            qEntityAlias.qReply.uuid.`as`(alias.columnReplyUuid),
            qEntityAlias.qReply.content.`as`(alias.columnReplyContent),
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryReplyLike, "id").countDistinct().`as`(alias.columnReplyLikesCount), // reply likeCount
            qEntityAlias.qComment.modifiedCnt.`as`(alias.columnReplyModifiedCnt),
            Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qReply, "created_dt").`as`(alias.columnCreatedDate), // created_dt
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
                Expressions.stringPath(qEntityAlias.qReplyLike2, "like_status").eq(LikeStatus.LIKED.name)
            )
    }
}