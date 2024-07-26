package com.example.myvopiserver.infrastructure.custom

import com.example.myvopiserver.common.enums.CommentStatus
import com.example.myvopiserver.common.enums.SearchFilter
import com.example.myvopiserver.domain.QComment
import com.example.myvopiserver.domain.QCommentLike
import com.example.myvopiserver.domain.QReply
import com.example.myvopiserver.domain.command.CommentSearchCommand
import com.example.myvopiserver.domain.role.QUser
import com.example.myvopiserver.infrastructure.custom.expression.AliasExpressions
import com.example.myvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.jpa.sql.JPASQLQuery
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
class CustomCommentReaderStoreImpl(
    private val em: EntityManager,
    private val jpaQueryFactory: JPAQueryFactory,
    private val mysqlTemplates: SQLTemplates,
    private val alias: AliasExpressions,
): CustomCommentReaderStore {

    private final val maxFetchCnt = 10L;

    /**
     * select c.uuid
     *      , c.content
     *      , c.modified_cnt
     *      , u.uuid
     *      , u.user_id
     * 	    , count(distinct cl.id) as like_count
     *      , count(distinct r.id) as reply_count
     *      , c.created_dt
     *   from comment c
     *   left join comment_like cl on cl.comment_id = c.id
     *   left join (
     *   	        select r2.*
     *   	          from reply r2
     *   	         where 1=1
     *   	           and r2.status IN ('SHOW', 'FLAGGED')
     *              ) r on r.comment_id = c.id
     *   join `user` u ON u.id = c.user_id
     *  where 1=1
     *    and c.video_id = 1
     *    and c.status in ('SHOW', 'FLAGGED')
     *  group by c.id
     *  order by like_count desc
     *  limit 10 offset 0;
    * */
    override fun pageableCommentAndReplyFindByVideo(command: CommentSearchCommand): List<Tuple> {

        val jpaSqlQuery: JPASQLQuery<*> = JPASQLQuery<Any>(em, mysqlTemplates)
        val replySubQueryAlias = Expressions.stringPath("r2");

        val qComment = QComment.comment
        val qCommentLike = QCommentLike.commentLike
        val qReply = QReply.reply
        val qUser = QUser.user

        val query = jpaSqlQuery
            .select(
                qComment.content.`as`(alias.commentContentAlias),
                qComment.modifiedCnt.`as`(alias.commentModifiedCntAlias),
                qUser.userId.`as`(alias.userIdAlias),
                qCommentLike.id.countDistinct().`as`(alias.commentLikesCountAlias), // likeCount
                Expressions.numberPath(Long::class.java, replySubQueryAlias, "id").countDistinct().`as`(alias.replyCountAlias), // replyCount
                Expressions.datePath(LocalDateTime::class.java, qComment, "created_dt").`as`(alias.createdDateAlias) // created_dt
            )
            .from(qComment)
            .leftJoin(qCommentLike).on(Expressions.numberPath(Long::class.javaObjectType, qCommentLike, "comment_id").eq(qComment.id))
            .leftJoin(
                JPAExpressions
                    .select(
                        Expressions.numberPath(Long::class.java, qReply, "comment_id").`as`("comment_id"),
                        qReply.id
                    )
                    .from(qReply)
                    .where(
                        Expressions.stringPath(qReply, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name)
                    )
                , replySubQueryAlias
            ).on((Expressions.numberPath(Long::class.javaObjectType, replySubQueryAlias, "comment_id")).eq(qComment.id))
            .join(qUser).on(Expressions.numberPath(Long::class.javaObjectType, qComment, "user_id").eq(qUser.id))
            .where(
                Expressions.numberPath(Long::class.java, qComment, "video_id").eq(command.videoId),
                Expressions.stringPath(qComment, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name)
            )
            .groupBy(qComment.id)
        if(command.filter == SearchFilter.POPULAR) {
            query.orderBy(alias.commentLikesCountAlias.desc())
        } else {
            query.orderBy(alias.createdDateAlias.desc())
        }
        return query.limit(maxFetchCnt)
            .offset(command.reqPage.toLong() * maxFetchCnt)
            .fetch()
    }
}