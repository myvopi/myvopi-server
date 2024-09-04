package com.example.cronserver.infrasturcture

import com.example.cronserver.domain.interfaces.TableReader
import com.example.cronserver.infrasturcture.custom.jpql.repository.TableReaderJpql
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class TableReaderImpl(
    private val tableReaderJpql: TableReaderJpql,
): TableReader {

    @Transactional(readOnly = true)
    override fun checkIfQuartzTableExistsJpqlRequest(): Boolean {
        return tableReaderJpql.checkIfQuartzTableExists()
    }
}