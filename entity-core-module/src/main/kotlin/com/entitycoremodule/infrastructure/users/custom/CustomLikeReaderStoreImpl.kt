package com.entitycoremodule.infrastructure.users.custom

import com.commoncoremodule.enums.LikeStatus
import com.entitycoremodule.command.CommentLikePostCommand
import com.entitycoremodule.command.ReplyLikePostCommand
import com.entitycoremodule.domain.like.CommentLike
import com.entitycoremodule.domain.like.ReplyLike
import com.entitycoremodule.infrastructure.users.custom.repository.CustomLikeReaderStore
import com.entitycoremodule.infrastructure.users.custom.alias.QEntityAlias
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class CustomLikeReaderStoreImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val em: EntityManager,
    private val qEntityAlias: QEntityAlias,
): CustomLikeReaderStore {

    /**
     * select cl.*
     *   from comment_like cl
     *  where 1=1
     *    and cl.comment_id = 1
     *    and cl.user_id = 1
    * */
    override fun findCommentLikeRequest(commentId: Long, userId: Long): CommentLike? {
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

    override fun saveCommentLikeRequest(command: CommentLikePostCommand) {
        val query = "INSERT INTO myviopi.comment_like " +
                "(created_dt, updated_dt, comment_id, user_id, like_status) " +
                "VALUES(NOW(), NOW(), ${command.commentId}, ${command.userId}, '${LikeStatus.LIKED.name}')"
        em.createNativeQuery(query).executeUpdate()
    }

    override fun findReplyLikeRequest(replyId: Long, userId: Long): ReplyLike? {
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

    override fun saveReplyLikeRequest(command: ReplyLikePostCommand) {
        val query = "INSERT INTO myviopi.reply_like " +
                "(created_dt, updated_dt, reply_id, user_id, like_status) " +
                "VALUES(NOW(), NOW(), ${command.replyId}, ${command.userId}, '${LikeStatus.LIKED.name}')"
        em.createNativeQuery(query).executeUpdate()
    }
}