package com.example.cronserver.common.config

import com.example.cronserver.domain.interfaces.TableReader
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
class SchemaInitializer(
    private val dataSource: DataSource,
    private val resourceLoader: ResourceLoader,
    private val tableReader: TableReader,
) {

    @PostConstruct
    fun initializeSchema() {
        val exists = tableReader.checkIfQuartzTableExistsJpqlRequest()
        if(!exists) {
            val resource = resourceLoader.getResource("classpath:schema\\schema.sql")
            val databasePopulator = ResourceDatabasePopulator(resource)
            databasePopulator.execute(dataSource)
        }
    }
}