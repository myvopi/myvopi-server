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
    final val columnCommentUuid: StringPath = Expressions.stringPath("commentUuid")
    final val columnCommentContent: StringPath = Expressions.stringPath("commentContent")
    final val columnCommentModifiedCnt: NumberPath<Int> = Expressions.numberPath(Int::class.javaObjectType, "commentModifiedCnt")
    final val columnUserUuid: StringPath = Expressions.stringPath("userUuid")
    final val columnUserId: StringPath = Expressions.stringPath("userId")
    final val columnCommentLikesCount: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "commentLikeCount")
    final val columnReplyCount: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "replyCount")
    final val columnCreatedDate = Expressions.datePath(LocalDateTime::class.javaObjectType, "createdDate")
    final val columnCreatedDateTuple: DatePath<Date> = Expressions.datePath(Date::class.javaObjectType, "createdDate")
    final val subQueryReply = Expressions.stringPath("r")
    final val columnUserLiked = Expressions.booleanPath("userLiked")
}