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
    "com.entitycoremodule.mapper",
    "com.entitycoremodule.domain.user",
    "com.entitycoremodule.domain.comment",
    "com.entitycoremodule.domain.email",
    "com.entitycoremodule.domain.like",
    "com.entitycoremodule.domain.reply",
    "com.entitycoremodule.domain.video",
    "com.entitycoremodule.infrastructure",
    "com.authcoremodule",
])
class BeanConfig {}