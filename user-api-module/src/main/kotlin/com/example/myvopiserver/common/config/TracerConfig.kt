package com.example.myvopiserver.common.config

import io.micrometer.tracing.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TracerConfig {

    @Bean
    fun tracer(): Tracer {
        return Tracer.NOOP
    }
}