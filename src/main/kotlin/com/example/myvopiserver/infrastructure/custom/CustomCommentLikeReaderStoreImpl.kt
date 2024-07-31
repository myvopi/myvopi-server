package com.example.myvopiserver.infrastructure.custom

import com.example.myvopiserver.common.enums.LikeStatus
import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.QComment
import com.example.myvopiserver.domain.QCommentLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.role.QUser
import com.example.myvopiserver.infrastructure.custom.repository.CustomCommentLikeReaderStore
import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.jpa.sql.JPASQLQuery
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class CustomCommentLikeReaderStoreImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val em: EntityManager,
    private val mysqlTemplates: SQLTemplates,
): CustomCommentLikeReaderStore {

    private fun constructJpaSqlQuery(): JPASQLQuery<*> {
        return JPASQLQuery<Any>(em, mysqlTemplates)
    }

    /**
     * select cl.*
     *   from comment_like cl
     *   join comment c on c.id = cl.comment_id
     *   join user u on u.id = cl.user_id
     *  where 1=1
     *    and cl.comment_id = 1
     *    and cl.user_id = 1
    * */
    override fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike? {
        val qCommentLike = QCommentLike.commentLike
        val qComment = QComment.comment
        val qUser = QUser.user

        return jpaQueryFactory
            .select(qCommentLike)
            .from(qCommentLike)
            .join(qComment).on(qComment.id.eq(qCommentLike.comment.id))
            .join(qUser).on(qUser.id.eq(qCommentLike.user.id))
            .where(
                qComment.id.eq(commentId),
                qUser.id.eq(userId)
            )
            .fetchOne()
    }

    override fun saveCommentLikeRequest(command: CommentLikePostCommand) {
        val query = "INSERT INTO myviopi.comment_like " +
                "(created_dt, updated_dt, comment_id, user_id, like_status) " +
                "VALUES(NOW(), NOW(), ${command.commentId}, ${command.userId}, '${LikeStatus.LIKED.name}')"
        em.createNativeQuery(query).executeUpdate()
    }
}