package com.example.cronserver.common.config

import com.querydsl.sql.MySQLTemplates
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryConfig {

    @PersistenceContext
    lateinit var em: EntityManager

    @Bean
    fun mysqlTemplates(): SQLTemplates {
        return MySQLTemplates.builder().build()
    }
}