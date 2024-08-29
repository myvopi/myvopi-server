package com.example.adminmyvopiserver.infrastructure.custom

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.LikeStatus
import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.alias.BasicAlias
import com.example.adminmyvopiserver.infrastructure.custom.alias.QEntityAlias
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.sql.JPASQLQuery
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CustomCommentReaderStoreImpl(
    private val mysqlTemplates: SQLTemplates,
    private val em: EntityManager,
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
): CustomCommentReaderStore {

    private val maxFetchCnt = 10L

    override fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple> {
        return constructCommentSelectQuery()
            .from(qEntityAlias.qComment)
            .leftJoin(constructCommentLikeSubQuery(), alias.subQueryCommentLike)
                .on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryCommentLike, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(constructReplySubQuery(), alias.subQueryReply)
                .on((Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id")).eq(qEntityAlias.qComment.id))
            .join(qEntityAlias.qVideo, qEntityAlias.qVideo).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "video_id").eq(qEntityAlias.qVideo.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "user_id").eq(qEntityAlias.qUser.id))
            .groupBy(qEntityAlias.qComment.id)
            .orderBy(Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qComment, "created_dt").desc())
            .limit(maxFetchCnt)
            .offset(command.reqPage.toLong() * maxFetchCnt)
            .fetch()
    }

    override fun findCommentsBetweenDateRequest(fromDate: LocalDateTime, toDate: LocalDateTime, reqPage: Int): List<Tuple> {
        return constructCommentSelectQuery()
            .from(qEntityAlias.qComment)
            .leftJoin(constructCommentLikeSubQuery(), alias.subQueryCommentLike)
                .on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryCommentLike, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(constructReplySubQuery(), alias.subQueryReply)
                .on((Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id")).eq(qEntityAlias.qComment.id))
            .join(qEntityAlias.qVideo).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "video_id").eq(qEntityAlias.qVideo.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "user_id").eq(qEntityAlias.qUser.id))
            .where(Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qComment, "created_dt").between(fromDate, toDate))
            .groupBy(qEntityAlias.qComment.id)
            .orderBy(Expressions.datePath(LocalDateTime::class.java, qEntityAlias.qComment, "created_dt").desc())
            .limit(maxFetchCnt)
            .offset(reqPage.toLong() * maxFetchCnt)
            .fetch()
    }

    override fun updateCommentsStatusDeleteAdminByUserRequest(internalUserCommand: InternalUserCommand) {
        val query = "UPDATE comment c" +
                    "  JOIN `user` u ON u.id = c.user_id" +
                    "   SET comment_status = '${CommentStatus.DELETED_ADMIN.name}'" +
                    " WHERE c.user_id = ${internalUserCommand.id}" +
                    "   AND u.user_id = '${internalUserCommand.userId}'" +
                    "   AND u.uuid = '${internalUserCommand.uuid}'" +
                    "   AND u.email = '${internalUserCommand.email}'"
        em.createNativeQuery(query).executeUpdate()
    }

    private fun constructCommentSelectQuery(): JPASQLQuery<Tuple> {
        val query = JPASQLQuery<Any>(em, mysqlTemplates)
        return query.select(
            qEntityAlias.qComment,
            qEntityAlias.qUser.userId.`as`(alias.columnUserId),
            Expressions.numberPath(Long::class.java, alias.subQueryCommentLike, "id").countDistinct().`as`(alias.columnCommentLikesCount), // like count
            Expressions.numberPath(Long::class.java, alias.subQueryReply, "id").countDistinct().`as`(alias.columnReplyCount), // reply count
            Expressions.stringPath(qEntityAlias.qVideo, "video_id").`as`(alias.columnVideoId)   // video id
        )
    }

    private fun constructCommentLikeSubQuery(): JPQLQuery<Tuple> {
        return JPAExpressions
            .select(
                qEntityAlias.qCommentLike.id,
                Expressions.numberPath(Long::class.java, qEntityAlias.qCommentLike, "comment_id").`as`("comment_id"),
            )
            .from(qEntityAlias.qCommentLike)
            .where(Expressions.stringPath(qEntityAlias.qCommentLike, "like_status").eq(LikeStatus.LIKED.name))
    }

    private fun constructReplySubQuery(): JPQLQuery<Tuple> {
        return JPAExpressions
            .select(
                Expressions.numberPath(Long::class.java, qEntityAlias.qReply2, "comment_id").`as`("comment_id"),
                qEntityAlias.qReply2.id
            )
            .from(qEntityAlias.qReply2)
            .where(Expressions.stringPath(qEntityAlias.qReply2, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name))
    }
}