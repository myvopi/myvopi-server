package com.example.myvopiserver.infrastructure.custom

import com.commoncoremodule.enums.LikeStatus
import com.example.myvopiserver.domain.CommentLike
import com.example.myvopiserver.domain.ReplyLike
import com.example.myvopiserver.domain.command.CommentLikePostCommand
import com.example.myvopiserver.domain.command.ReplyLikePostCommand
import com.example.myvopiserver.infrastructure.custom.alias.QEntityAlias
import com.example.myvopiserver.infrastructure.custom.repository.CustomLikeReaderStore
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

    // TODO need to refactor, might cause sql injection
    override fun saveCommentLikeRequest(command: CommentLikePostCommand) {
        val query = "INSERT INTO comment_like " +
                "(createdDt, updatedDt, comment_id, user_id, status) " +
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
        val query = "INSERT INTO reply_like " +
                "(createdDt, updatedDt, reply_id, user_id, status) " +
                "VALUES(NOW(), NOW(), ${command.replyId}, ${command.userId}, '${LikeStatus.LIKED.name}')"
        em.createNativeQuery(query).executeUpdate()
    }
}