package com.example.cronserver.infrasturcture.custom.jpql

import com.example.cronserver.infrasturcture.custom.jpql.repository.TableReaderJpql
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class TableReaderJpqlImpl(
    private val em: EntityManager,
): TableReaderJpql {

    override fun checkIfQuartzTableExists(): Boolean {
        val query = "SELECT COUNT(*) " +
                    "  FROM information_schema.tables ist " +
                    " WHERE ist.table_schema = DATABASE()" +
                    "   AND table_name = 'QRTZ_JOB_DETAILS' "
        val result = em.createNativeQuery(query).singleResult as Long
        return result.toInt() == 1
    }
}