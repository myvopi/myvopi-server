package com.example.myvopiserver.infrastructure.custom.expression

import com.example.myvopiserver.domain.QComment
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import org.springframework.stereotype.Component

@Component
class CommentQueryExpressions {

    fun eqVideoId(id: Long?): BooleanExpression? {
        return if(id == null) null
        else {
            Expressions.numberPath(Long::class.java, QComment.comment, "video_id").eq(id)
        }
    }
}