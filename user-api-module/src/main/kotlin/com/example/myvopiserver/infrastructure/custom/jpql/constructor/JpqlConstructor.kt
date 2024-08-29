package com.example.myvopiserver.infrastructure.custom.jpql.constructor

import com.example.myvopiserver.infrastructure.custom.jpql.column.ColumnNames
import org.springframework.stereotype.Component

@Component
class JpqlConstructor(
    private val columnNames: ColumnNames,
) {

    fun constructLikeInsertQuery(
        tableName: String?,
        setEntityId: String,
    ): String {
        return "INSERT INTO $tableName (" +
               " ${columnNames.createdDt} ," +
               " ${columnNames.updatedDt} ," +
               " ${columnNames.status} ," +
               " $setEntityId ," +
               " ${columnNames.userId} " +
               ") VALUES (" +
               " :createdDt , " +
               " :updatedDt , " +
               " :status , " +
               " :$setEntityId , " +
               " :user_id " +
               ")"
    }
}