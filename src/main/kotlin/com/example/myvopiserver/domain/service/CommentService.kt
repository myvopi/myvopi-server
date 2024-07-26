package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.command.CommentSearchCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.infrastructure.custom.expression.AliasExpressions
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val alias: AliasExpressions,
) {

    @Transactional(readOnly = true)
    fun findComments(command: CommentSearchCommand): List<Tuple> {
        return commentReaderStore.findComments(command)
    }

    fun constructCommentBaseInfo(result: List<Tuple>): List<CommentBaseInfo> {
        return result.map { tuple ->
            CommentBaseInfo(
                commentUuid = tuple.get(alias.commentUuidAlias)!!,
                content = tuple.get(alias.commentContentAlias)!!,
                userId = tuple.get(alias.userIdAlias)!!,
                likeCount = tuple.get(alias.commentLikesCountAlias)!!,
                replyCount = tuple.get(alias.replyCountAlias)!!,
                createdDate = tuple.get(alias.createdDateTupleAlias)!!.toStrings("yyyy-MM-dd HH:mm:ss"),
                modified = tuple.get(alias.commentModifiedCntAlias)!! > 0,
            )
        }
    }
}