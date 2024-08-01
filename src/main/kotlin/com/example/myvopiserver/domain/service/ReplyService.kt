package com.example.myvopiserver.domain.service

import com.example.myvopiserver.common.util.extension.toStrings
import com.example.myvopiserver.domain.command.ReplySearchCommand
import com.example.myvopiserver.domain.info.ReplyBaseInfo
import com.example.myvopiserver.domain.interfaces.ReplyReaderStore
import com.example.myvopiserver.infrastructure.custom.alias.BasicAlias
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class ReplyService(
    private val replyReaderStore: ReplyReaderStore,
    private val alias: BasicAlias,
) {

    // Db-transactions (readOnly)
    fun findReplies(command: ReplySearchCommand): List<Tuple> {
        return replyReaderStore.findRepliesRequest(command)
    }

    // Private & constructors
    private fun mapReplyBaseInfoOfResult(result: Tuple): ReplyBaseInfo {
        return ReplyBaseInfo(
            uuid = result.get(alias.columnReplyUuid)!!,
            content = result.get(alias.columnReplyContent)!!,
            userId = result.get(alias.columnUserId)!!,
            replyLikeCount = result.get(alias.columnReplyLikesCount)!!,
            modified = result.get(alias.columnReplyModifiedCnt)!! > 0,
            createdDate = result.get(alias.columnCreatedDateTuple)!!.toStrings("yyyy-MM-dd HH:mm:ss"),
            userLiked = result.get(alias.columnUserLiked)?: false,
        )
    }

    fun constructReplyBaseInfo(results: List<Tuple>): List<ReplyBaseInfo> {
        return results.map { result ->
            mapReplyBaseInfoOfResult(result)
        }
    }
}