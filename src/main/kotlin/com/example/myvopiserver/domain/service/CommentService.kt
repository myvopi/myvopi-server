package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.command.CommentSearchFromCommentCommand
import com.example.myvopiserver.domain.command.CommentSearchFromVideoCommand
import com.example.myvopiserver.domain.info.CommentBaseInfo
import com.example.myvopiserver.domain.interfaces.CommentReaderStore
import com.example.myvopiserver.infrastructure.custom.expression.AliasExpressions
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val alias: AliasExpressions,
) {

    fun findCommentsFromVideo(command: CommentSearchFromVideoCommand): List<Tuple> {
        return commentReaderStore.findCommentsFromVideoRequest(command)
    }

    fun findComments(command: CommentSearchFromCommentCommand): List<Tuple> {
        return commentReaderStore.findCommentsFromCommentRequest(command)
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