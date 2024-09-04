package com.example.cronserver.infrasturcture.custom.jpql.repository

import org.springframework.stereotype.Repository

@Repository
interface TableReaderJpql {

    fun checkIfQuartzTableExists(): Boolean
}