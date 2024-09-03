package com.example.myvopiserver.infrastructure.custom.jpql

import com.commoncoremodule.extension.getCurrentDateTime
import com.example.myvopiserver.common.util.ClassVariables.Companion.TablesNamesByClass
import com.example.myvopiserver.common.util.CodeGenerator.Companion.ipIdGenerator
import com.example.myvopiserver.domain.Ip
import com.example.myvopiserver.infrastructure.custom.jpql.column.ColumnNames
import com.example.myvopiserver.infrastructure.custom.jpql.constructor.JpqlConstructor
import com.example.myvopiserver.infrastructure.custom.jpql.repository.IpStoreJpql
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class IpStoreJpqlImpl(
    private val em: EntityManager,
    private val jpqlConstructor: JpqlConstructor,
    private val columnNames: ColumnNames,
): IpStoreJpql {

    private val tableNamesByClass = TablesNamesByClass

    override fun saveIp(host: String, port: Int, url: String) {
        val queryBuilder = StringBuilder()
        val tableName = tableNamesByClass[Ip::class]
        val query = queryBuilder.append(jpqlConstructor.constructIpInsertQuery(tableName))
            .toString()
        val currentDateTime = getCurrentDateTime()
        em.createNativeQuery(query)
            .setParameter(columnNames.id, ipIdGenerator())
            .setParameter(columnNames.createdDt, currentDateTime)
            .setParameter(columnNames.port, port)
            .setParameter(columnNames.host, host)
            .setParameter(columnNames.url, url)
            .executeUpdate()
    }
}