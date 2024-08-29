package com.example.adminmyvopiserver.common.config

import com.querydsl.sql.MySQLTemplates
import com.querydsl.sql.SQLTemplates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MySQLTemplateConfig {

    @Bean
    fun mysqlTemplates(): SQLTemplates {
        return MySQLTemplates.builder().build()
    }
}