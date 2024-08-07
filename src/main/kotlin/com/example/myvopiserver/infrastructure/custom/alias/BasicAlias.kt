package com.example.myvopiserver.infrastructure.custom.alias

import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class BasicAlias {
    // Common uses
    final val columnUserLiked = Expressions.booleanPath("userLiked")
    final val columnCreatedDate = Expressions.datePath(LocalDateTime::class.javaObjectType, "createdDate")
    final val columnCreatedDateTuple: DatePath<Date> = Expressions.datePath(Date::class.javaObjectType, "createdDate")
    // Comment uses
    final val columnCommentUuid: StringPath = Expressions.stringPath("commentUuid")
    final val columnCommentContent: StringPath = Expressions.stringPath("commentContent")
    final val columnCommentModifiedCnt: NumberPath<Int> = Expressions.numberPath(Int::class.javaObjectType, "commentModifiedCnt")
    final val columnUserUuid: StringPath = Expressions.stringPath("userUuid")
    final val columnUserId: StringPath = Expressions.stringPath("userId")
    final val columnCommentLikesCount: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "commentLikeCount")
    final val columnReplyCount: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "replyCount")
    final val subQueryReply = Expressions.stringPath("r")
    final val subQueryCommentLike = Expressions.stringPath("cl")
    // Reply uses
    final val columnReplyUuid: StringPath = Expressions.stringPath("replyUuid")
    final val columnReplyContent: StringPath = Expressions.stringPath("replyContent")
    final val subQueryReplyLike = Expressions.stringPath("rl")
    final val columnReplyLikesCount: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "replyLikeCount")
    final val columnReplyModifiedCnt: NumberPath<Int> = Expressions.numberPath(Int::class.javaObjectType, "replyModifiedCnt")
    // Entity uses
    final val reply = Expressions.stringPath("r")
    final val replyUser = Expressions.stringPath("ru")
    final val comment = Expressions.stringPath("c")
    final val commentUser = Expressions.stringPath("cu")
    final val video = Expressions.stringPath("v")
    final val videouser = Expressions.stringPath("vu")
}