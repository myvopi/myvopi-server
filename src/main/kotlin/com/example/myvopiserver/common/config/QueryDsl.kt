package com.example.myvopiserver.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDsl {

    @Configuration
    class QueryDslConfig {

        @PersistenceContext
        lateinit var em: EntityManager

        @Bean
        fun jpaQueryFactory(): JPAQueryFactory {
            return JPAQueryFactory(em)
        }
    }
}