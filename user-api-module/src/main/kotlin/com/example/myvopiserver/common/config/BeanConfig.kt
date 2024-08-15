package com.example.myvopiserver.common.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = [
    "com.entitycoremodule.domain",
])
@EnableJpaRepositories(basePackages = [
    "com.entitycoremodule.infrastructure"
])
@ComponentScan(basePackages = [
    "com.entitycoremodule.mapper",              // mapper
    "com.entitycoremodule.domain.user",         // Jpa, QEntity
    "com.entitycoremodule.domain.comment",      // Jpa, QEntity
    "com.entitycoremodule.domain.email",        // Jpa, QEntity
    "com.entitycoremodule.domain.like",         // Jpa, QEntity
    "com.entitycoremodule.domain.reply",        // Jpa, QEntity
    "com.entitycoremodule.domain.video",        // Jpa, QEntity
    "com.entitycoremodule.infrastructure",      // ReaderStore
    "com.authcoremodule",                       // Authentication, Filter
    "com.externalapimodule.mail",               // JavaMail
])
class BeanConfig {}