package com.example.myvopiserver.infrastructure.custom.jpql

import com.commoncoremodule.enums.LikeStatus
import com.commoncoremodule.extension.getCurrentDateTime
import com.example.myvopiserver.common.util.ClassResources.Companion.TablesNamesByClass
import com.example.myvopiserver.domain.*
import com.example.myvopiserver.domain.command.CommentLikePostRequestCommand
import com.example.myvopiserver.domain.command.ReplyLikePostRequestCommand
import com.example.myvopiserver.infrastructure.custom.jpql.column.ColumnNames
import com.example.myvopiserver.infrastructure.custom.jpql.repository.LikeReaderStoreJpql
import com.example.myvopiserver.infrastructure.custom.jpql.constructor.JpqlConstructor
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class LikeReaderStoreJpqlImpl(
    private val em: EntityManager,
    private val jpqlConstructor: JpqlConstructor,
    private val columnNames: ColumnNames,
): LikeReaderStoreJpql {

    private val tableNamesByClass = TablesNamesByClass

    override fun saveCommentLike(command: CommentLikePostRequestCommand) {
        val queryBuilder = StringBuilder()
        val tableName = tableNamesByClass[CommentLike::class]
        val query = queryBuilder.append(jpqlConstructor.constructLikeInsertQuery(tableName, columnNames.commentId))
            .toString()
        val currentDateTime = getCurrentDateTime()
        em.createNativeQuery(query)
            .setParameter(columnNames.createdDt, currentDateTime)
            .setParameter(columnNames.updatedDt, currentDateTime)
            .setParameter(columnNames.status, LikeStatus.LIKED.name)
            .setParameter(columnNames.commentId, command.commentId)
            .setParameter(columnNames.userId, command.userId)
            .executeUpdate()
    }

    override fun saveReplyLike(command: ReplyLikePostRequestCommand) {
        val queryBuilder = StringBuilder()
        val tableName = tableNamesByClass[ReplyLike::class]
        val query = queryBuilder.append(jpqlConstructor.constructLikeInsertQuery(tableName, columnNames.replyId))
            .toString()
        val currentDateTime = getCurrentDateTime()
        em.createNativeQuery(query)
            .setParameter(columnNames.createdDt, currentDateTime)
            .setParameter(columnNames.updatedDt, currentDateTime)
            .setParameter(columnNames.status, LikeStatus.LIKED.name)
            .setParameter(columnNames.replyId, command.replyId)
            .setParameter(columnNames.userId, command.userId)
            .executeUpdate()
    }
}