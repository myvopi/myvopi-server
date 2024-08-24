package com.example.adminmyvopiserver.infrastructure.custom

import com.commoncoremodule.enums.LikeStatus
import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.alias.BasicAlias
import com.example.adminmyvopiserver.infrastructure.custom.alias.QEntityAlias
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.sql.JPASQLQuery
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class CustomCommentReaderStoreImpl(
    private val mysqlTemplates: SQLTemplates,
    private val em: EntityManager,
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
): CustomCommentReaderStore {

    override fun findCommentsByUserRequest(command: CommentAdminSearchCommand): List<Tuple> {
        val maxFetchCnt = 100L
        val query = JPASQLQuery<Any>(em, mysqlTemplates)

        return query.select(
            qEntityAlias.qComment,
            Expressions.numberPath(Long::class.java, alias.subQueryCommentLike, "id").countDistinct().`as`(alias.columnCommentLikesCount)
        )
        .from(qEntityAlias.qComment)
        .leftJoin(
            JPAExpressions
                .select(
                    qEntityAlias.qCommentLike.id,
                    Expressions.numberPath(Long::class.java, qEntityAlias.qCommentLike, "comment_id").`as`("comment_id"),
                )
                .from(qEntityAlias.qCommentLike)
                .where(
                    Expressions.stringPath(qEntityAlias.qCommentLike, "like_status").eq(LikeStatus.LIKED.name)
                ),
            alias.subQueryCommentLike
        ).on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryCommentLike, "comment_id").eq(qEntityAlias.qComment.id))
        .fetch()
    }
}