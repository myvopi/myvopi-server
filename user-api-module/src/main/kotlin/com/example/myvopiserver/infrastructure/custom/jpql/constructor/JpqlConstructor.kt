package com.example.myvopiserver.infrastructure.custom.jpql.constructor

import com.example.myvopiserver.infrastructure.custom.jpql.column.ColumnNames
import org.springframework.stereotype.Component

@Component
class JpqlConstructor(
    columnNames: ColumnNames,
) {

    private val createdDtName = columnNames.createdDt
    private val updatedDtName = columnNames.updatedDt
    private val statusName = columnNames.status
    private val userIdName = columnNames.userId
    private val idName = columnNames.id
    private val hostName = columnNames.host
    private val portName = columnNames.port
    private val urlName = columnNames.url

    fun constructLikeInsertQuery(
        tableName: String?,
        setEntityId: String,
    ): StringBuilder {
        return StringBuilder()
            .append(
                "INSERT INTO $tableName (" +
                " $createdDtName ," +
                " $updatedDtName ," +
                " $statusName ," +
                " $setEntityId ," +
                " $userIdName" +
                ") VALUES (" +
                " :$createdDtName ," +
                " :$updatedDtName ," +
                " :$statusName ," +
                " :$setEntityId ," +
                " :$userIdName" +
                ")"
            )
    }

    fun constructIpInsertQuery(
        tableName: String?,
    ): StringBuilder {
        return StringBuilder()
            .append(
                "INSERT INTO $tableName (" +
                " $idName ," +
                " $createdDtName ," +
                " $portName ," +
                " $hostName ," +
                " $urlName" +
                ") VALUES (" +
                " :$idName ," +
                " :$createdDtName ," +
                " :$portName ," +
                " :$hostName ," +
                " :$urlName" +
                ")"
            )
    }
}