package com.example.myvopiserver.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.sql.MySQLTemplates
import com.querydsl.sql.SQLTemplates
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDsl {

    @PersistenceContext
    lateinit var em: EntityManager

    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }

    @Bean
    fun mysqlTemplates(): SQLTemplates {
        return MySQLTemplates.builder().build()
    }
}