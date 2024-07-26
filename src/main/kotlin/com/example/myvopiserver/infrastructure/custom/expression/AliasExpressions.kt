package com.example.myvopiserver.infrastructure.custom.expression

import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class AliasExpressions {
    val commentUuidAlias: StringPath = Expressions.stringPath("commentUuid")
    val commentContentAlias: StringPath = Expressions.stringPath("commentContent")
    val commentModifiedCntAlias: NumberPath<Int> = Expressions.numberPath(Int::class.javaObjectType, "commentModifiedCnt")
    val userUuidAlias: StringPath = Expressions.stringPath("userUuid")
    val userIdAlias: StringPath = Expressions.stringPath("userId")
    val commentLikesCountAlias: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "commentLikeCount")
    val replyCountAlias: NumberPath<Long> = Expressions.numberPath(Long::class.javaObjectType, "replyCount")
    val createdDateAlias: DatePath<LocalDateTime> = Expressions.datePath(LocalDateTime::class.javaObjectType, "createdDate")
    val createdDateTupleAlias: DatePath<Date> = Expressions.datePath(Date::class.javaObjectType, "createdDate")
}