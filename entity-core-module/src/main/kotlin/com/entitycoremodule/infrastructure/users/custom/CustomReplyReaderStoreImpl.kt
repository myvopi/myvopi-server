package com.entitycoremodule.infrastructure.users.custom

import com.commoncoremodule.enums.CommentStatus
import com.entitycoremodule.command.ReplySearchCommand
import com.entitycoremodule.command.SingleReplySearchCommand
import com.entitycoremodule.infrastructure.alias.BasicAlias
import com.entitycoremodule.infrastructure.alias.QEntityAlias
import com.entitycoremodule.infrastructure.users.custom.queryDsl.QueryConstructor
import com.entitycoremodule.infrastructure.users.custom.repository.CustomReplyReaderStore
import com.querydsl.core.Tuple
import com.querydsl.core.types.dsl.Expressions
import org.springframework.stereotype.Repository

@Repository
class CustomReplyReaderStoreImpl(
    private val queryConstructor: QueryConstructor,
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
): CustomReplyReaderStore {

    private val maxFetchCnt = 10L

    /**
     * select r.uuid
     *      , r.content
     *      , u.user_id
     *      , count(distinct rl.id) as reply_like
     *      , r.modified_cnt
     *      , r.created_dt
     *      , ifnull((select true or false
     *                  from reply_like rl2
     *                  join reply r2 ON rl2.reply_id = r2.id
     *                 where 1=1
     *                   and r2.id = r.id
     *                   and rl2.user_id = 1
     *                   and rl2.like_status = 'LIKED'), 0) as user_liked
     *   from reply r
     *   left join comment c on c.id = r.comment_id
     *   left join (select rl2.*
     *                from reply_like rl2
     *               where 1=1
     *                 and rl2.like_status = 'LIKED') rl on r.id = rl.reply_id
     *   join `user` u on u.id = r.user_id
     *  where 1=1
     *    and c.uuid = 'dcdb55cd-3bb8-4cf5-97af-23fe7e6d52be'
     *    and c.comment_status in ('SHOW', 'FLAGGED')
     *    and r.comment_status in ('SHOW', 'FLAGGED')
     *  group by r.id
     *  order by r.created_dt desc;
    * */
    override fun findRepliesRequest(command: ReplySearchCommand): List<Tuple> {
        return queryConstructor.verifyAuthAndConstructReplySelectQuery(command.internalUserCommand)
            .from(qEntityAlias.qReply)
            .leftJoin(qEntityAlias.qComment).on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(queryConstructor.constructFilteredReplyLikeSubQuery(), alias.subQueryReplyLike)
                .on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReplyLike, "reply_id").eq(qEntityAlias.qReply.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qReply, "user_id").eq(qEntityAlias.qUser.id))
            .where(
                Expressions.stringPath(qEntityAlias.qComment, "uuid").eq(command.commentUuid),
                Expressions.stringPath(qEntityAlias.qComment, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name),
                Expressions.stringPath(qEntityAlias.qReply, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name),
            )
            .groupBy(qEntityAlias.qReply.id)
            .orderBy(alias.columnCreatedDate.desc())
            .limit(maxFetchCnt)
            .offset(command.reqPage.toLong() * maxFetchCnt)
            .fetch()
    }

    override fun findReplyRequest(command: SingleReplySearchCommand): Tuple? {
        return queryConstructor.constructAuthCommentSelectQuery(command.internalUserCommand)
            .from(qEntityAlias.qReply)
            .leftJoin(qEntityAlias.qComment).on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReply, "comment_id").eq(qEntityAlias.qComment.id))
            .leftJoin(queryConstructor.constructFilteredReplyLikeSubQuery(), alias.subQueryReplyLike)
                .on(Expressions.numberPath(Long::class.javaObjectType, alias.subQueryReplyLike, "reply_id").eq(qEntityAlias.qReply.id))
            .join(qEntityAlias.qUser).on(Expressions.numberPath(Long::class.javaObjectType, qEntityAlias.qReply, "user_id").eq(qEntityAlias.qUser.id))
            .where(
                Expressions.stringPath(qEntityAlias.qReply, "uuid").eq(command.replyUuid),
                Expressions.stringPath(qEntityAlias.qComment, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name),
                Expressions.stringPath(qEntityAlias.qReply, "status").`in`(CommentStatus.SHOW.name, CommentStatus.FLAGGED.name),
            )
            .groupBy(qEntityAlias.qReply.id)
            .fetchOne()
    }
}