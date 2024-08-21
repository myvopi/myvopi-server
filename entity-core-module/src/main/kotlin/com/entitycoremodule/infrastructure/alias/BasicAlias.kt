package com.entitycoremodule.infrastructure.alias

import com.querydsl.core.types.dsl.Expressions
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class BasicAlias {
    // Common uses
    final val columnUserLiked = Expressions.booleanPath("userLiked")
    final val columnCreatedDate = Expressions.datePath(LocalDateTime::class.javaObjectType, "createdDate")
    final val columnCreatedDateTuple = Expressions.datePath(Date::class.javaObjectType, "createdDate")
    // Comment uses
    final val columnCommentUuid = Expressions.stringPath("commentUuid")
    final val columnCommentContent = Expressions.stringPath("commentContent")
    final val columnCommentModifiedCnt = Expressions.numberPath(Int::class.javaObjectType, "commentModifiedCnt")
    final val columnUserId = Expressions.stringPath("userId")
    final val columnCommentLikesCount = Expressions.numberPath(Long::class.javaObjectType, "commentLikeCount")
    final val columnReplyCount = Expressions.numberPath(Long::class.javaObjectType, "replyCount")
    final val subQueryReply = Expressions.stringPath("r")
    final val subQueryCommentLike = Expressions.stringPath("cl")
    // Reply uses
    final val columnReplyUuid = Expressions.stringPath("replyUuid")
    final val columnReplyContent = Expressions.stringPath("replyContent")
    final val subQueryReplyLike = Expressions.stringPath("rl")
    final val columnReplyLikesCount = Expressions.numberPath(Long::class.javaObjectType, "replyLikeCount")
    final val columnReplyModifiedCnt = Expressions.numberPath(Int::class.javaObjectType, "replyModifiedCnt")
}