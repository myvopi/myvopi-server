package com.example.myvopiserver.infrastructure.custom

import com.commoncoremodule.enums.CommentStatus
import com.commoncoremodule.enums.SearchFilter
import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.example.myvopiserver.domain.command.SingleCommentSearchCommand
import com.example.myvopiserver.infrastructure.custom.alias.BasicAlias
import com.example.myvopiserver.infrastructure.custom.alias.QEntityAlias
import com.example.myvopiserver.infrastructure.custom.queryDsl.QueryConstructor
import com.example.myvopiserver.infrastructure.custom.repository.CustomCommentReaderStore
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import org.springframework.stereotype.Repository

// TODO need to change all values to object oriented
@Repository
class CustomCommentReaderStoreImpl(
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
    private val queryConstructor: QueryConstructor,
): CustomCommentReaderStore {

    private val maxFetchCnt = 10L

    /**
     * <- comment search from initial video request api ->
     *  select c.uuid
     *       , c.content
     *       , c.modified_cnt
     *       , u.uuid
     *       , u.user_id
     *       , count(distinct cl.id) as like_count
     *       , count(distinct r.id) as reply_count
     *       , c.created_dt
     *       , ifnull((select true or false
     *                   from comment_like cl2
     *                   join comment c2 ON cl2.comment_id = c2.id
     *                  where 1=1
     *                    and c2.video_id = c.video_id
     *                    and c2.id = c.id
     *                    and cl2.user_id = 1
     *                    and cl2.like_status = 'LIKED'), 0) as user_liked
     *    from comment c
     *    left join (select cl3.*
     *                 from comment_like cl3
     *                where 1=1
     *                  and cl3.like_status = 'LIKED') cl on cl.comment_id = c.id
     *    left join (select r2.*
     *          	   from reply r2
     *          	  where 1=1
     *          	    and r2.status IN ('SHOW', 'FLAGGED')) r on r.comment_id = c.id
     *    join `user` u ON u.id = c.user_id
     *    join video v on v.id = c.video_id
     *   where 1=1
     *     and c.video_id = 1
     *     and c.comment_status  in ('SHOW', 'FLAGGED')
     *     and v.video_type = 'YT_VIDEO'
     *   group by c.id
     *   order by like_count desc
     *   limit 10 offset 0;
    * */
    override fun pageableCommentAndReplyFromVideoRequest(command: CommentSearchFromVideoCommand): List<Tuple> {
        return queryConstructor.verifyAuthAndConstructCommentSelectQuery(command.internalUserCommand)
            .from(qEntityAlias.qComment)
            .leftJoin(queryConstructor.constructFilteredCommentLikeSubQuery(), alias.subQueryCommentLike).on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryCommentLike, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(queryConstructor.constructReplySubQuery(), alias.subQueryReply).on((Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id")).eq(qEntityAlias.qComment.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "user_id").eq(qEntityAlias.qUser.id))
            .join(qEntityAlias.qVideo).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "video_id").eq(qEntityAlias.qVideo.id))
            .where(
                Expressions.numberPath(Long::class.java, qEntityAlias.qComment, "video_id").eq(command.videoId),
                Expressions.stringPath(qEntityAlias.qComment, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name),
                Expressions.stringPath(qEntityAlias.qVideo, "videoType").eq(command.videoType.name)
            )
            .groupBy(qEntityAlias.qComment.id)
            .orderBy(
                if(command.filter == SearchFilter.POPULAR) alias.columnCommentLikesCount.desc()
                else alias.columnCreatedDate.desc()
            )
            .limit(maxFetchCnt)
            .offset(command.reqPage.toLong() * maxFetchCnt)
            .fetch()
    }

    override fun pageableCommentAndReplyFromCommentRequest(command: CommentSearchFromCommentCommand): List<Tuple> {
        return queryConstructor.verifyAuthAndConstructCommentSelectQuery(command.internalUserCommand)
            .from(qEntityAlias.qComment)
            .leftJoin(queryConstructor.constructFilteredCommentLikeSubQuery(), alias.subQueryCommentLike).on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryCommentLike, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(queryConstructor.constructReplySubQuery(), alias.subQueryReply).on((Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id")).eq(qEntityAlias.qComment.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "user_id").eq(qEntityAlias.qUser.id))
            .join(qEntityAlias.qVideo).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "video_id").eq(qEntityAlias.qVideo.id))
            .where(
                Expressions.stringPath(qEntityAlias.qVideo, "videoId").eq(command.videoId),
                Expressions.stringPath(qEntityAlias.qComment, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name),
                Expressions.stringPath(qEntityAlias.qVideo, "videoType").eq(command.videoType.name)
            )
            .groupBy(qEntityAlias.qComment.id)
            .orderBy(
                if(command.filter == SearchFilter.POPULAR) alias.columnCommentLikesCount.desc()
                else alias.columnCreatedDate.desc()
            )
            .limit(maxFetchCnt)
            .offset(command.reqPage.toLong() * maxFetchCnt)
            .fetch()
    }

    override fun findCommentRequest(command: SingleCommentSearchCommand): Tuple? {
        return queryConstructor.constructAuthCommentSelectQuery(command.internalUserCommand)
            .from(qEntityAlias.qComment)
            .leftJoin(queryConstructor.constructFilteredCommentLikeSubQuery(), alias.subQueryCommentLike).on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryCommentLike, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(queryConstructor.constructReplySubQuery(), alias.subQueryReply).on((Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id")).eq(qEntityAlias.qComment.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "user_id").eq(qEntityAlias.qUser.id))
            .join(qEntityAlias.qVideo).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qComment, "video_id").eq(qEntityAlias.qVideo.id))
            .where(
                qEntityAlias.qUser.uuid.eq(command.internalUserCommand.uuid),
                Expressions.stringPath(qEntityAlias.qUser, "userId").eq(command.internalUserCommand.userId),
                Expressions.stringPath(qEntityAlias.qVideo, "videoId").eq(command.videoId),
                Expressions.stringPath(qEntityAlias.qVideo, "videoType").eq(command.videoType.name),
                qEntityAlias.qComment.uuid.eq(command.commentUuid)
            )
            .groupBy(qEntityAlias.qComment.id)
            .fetchOne()
    }
}