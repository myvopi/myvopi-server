package com.example.myvopiserver.common.config

import com.example.myvopiserver.common.util.extension.EHCacheEventLogger
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.MemoryUnit
import org.ehcache.event.EventType
import org.ehcache.impl.config.event.DefaultCacheEventListenerConfiguration
import org.ehcache.jsr107.Eh107Configuration
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import javax.cache.CacheManager
import javax.cache.Caching
import javax.cache.spi.CachingProvider

@EnableCaching
@Configuration
class EHCacheConfig {

    @Bean
    fun ehCacheManager(): CacheManager {

        val cacheEventConfig =
            DefaultCacheEventListenerConfiguration(
                setOf(
                    EventType.CREATED,
                    EventType.REMOVED,
                    EventType.UPDATED,
                    EventType.EXPIRED,
                ),
                EHCacheEventLogger()
            )

        val cacheConfigurationBuilder = CacheConfigurationBuilder
            .newCacheConfigurationBuilder(
                Any::class.java,
                Any::class.java,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(500, MemoryUnit.KB)
                    .build()
            )
            .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
            .add(cacheEventConfig)
            .build()

        val cachingProvider: CachingProvider = Caching.getCachingProvider()
        val cacheManager: CacheManager = cachingProvider.cacheManager
        val configuration: javax.cache.configuration.Configuration<Any, Any> =
            Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfigurationBuilder)

        cacheManager.createCache("cacheStore", configuration)

        return cacheManager
    }
}