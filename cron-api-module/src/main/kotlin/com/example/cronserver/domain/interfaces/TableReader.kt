package com.example.cronserver.domain.interfaces

interface TableReader {

    fun checkIfQuartzTableExistsJpqlRequest(): Boolean
}