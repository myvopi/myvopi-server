package com.example.adminmyvopiserver.domain.service

import com.commoncoremodule.extension.getTodayBetweenDates
import com.commoncoremodule.extension.toStrings
import com.example.adminmyvopiserver.domain.command.CommentAdminSearchCommand
import com.example.adminmyvopiserver.domain.command.InternalCommentCommand
import com.example.adminmyvopiserver.domain.command.InternalUserCommand
import com.example.adminmyvopiserver.domain.info.CommentBaseInfo
import com.example.adminmyvopiserver.domain.interfaces.CommentReaderStore
import com.example.adminmyvopiserver.domain.mapper.CommentMapper
import com.example.adminmyvopiserver.infrastructure.custom.alias.BasicAlias
import com.example.adminmyvopiserver.infrastructure.custom.alias.QEntityAlias
import com.querydsl.core.Tuple
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReaderStore: CommentReaderStore,
    private val commentMapper: CommentMapper,
    private val alias: BasicAlias,
    private val qEntityAlias: QEntityAlias,
) {

    // Db-transactions (readOnly)
    fun findTodayComments(reqPage: Int): List<InternalCommentCommand> {
        val todayDateCommand = getTodayBetweenDates()
        val results = commentReaderStore.findCommentsBetweenDate(todayDateCommand.fromDate, todayDateCommand.toDate)
        return results.map { commentMapper.to(it) }
    }

    fun findTodayCommentsRequest(reqPage: Int): List<Tuple> {
        val todayDateCommand = getTodayBetweenDates()
        return commentReaderStore.findCommentsBetweenDateRequest(todayDateCommand.fromDate, todayDateCommand.toDate, reqPage)
    }

    fun findCommentsByUser(command: CommentAdminSearchCommand): List<Tuple> {
        return commentReaderStore.findCommentsByUserRequest(command)
    }

    // Db-transactions
    fun deleteAllCommentsDueToBan(internalUserCommand: InternalUserCommand) {
        commentReaderStore.updateCommentsStatusDeleteAdminByUserRequest(internalUserCommand)
    }

    // Private & constructors
    fun mapCommentBaseInfoOfResults(results: List<Tuple>): List<CommentBaseInfo> {
        return results.mapNotNull { result ->
            result.get(qEntityAlias.qComment)
                ?.let { comment ->
                    CommentBaseInfo(
                        uuid = comment.uuid,
                        content = comment.content,
                        modifiedCnt = comment.modifiedCnt,
                        status = comment.status.name,
                        verificationStatus = comment.verificationStatus.name,
                        videoId = result.get(alias.columnVideoId),
                        userId = result.get(alias.columnUserId),
                        likeCount = result.get(alias.columnCommentLikesCount),
                        replyCount = result.get(alias.columnReplyCount),
                        createdDate = comment.createdDt?.toStrings("yyyy-MM-dd HH:mm:ss"),
                        modified = comment.modifiedCnt,
                    )
                }
        }
    }
}