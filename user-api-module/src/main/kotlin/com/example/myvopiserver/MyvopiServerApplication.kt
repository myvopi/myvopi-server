package com.example.myvopiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
// TODO --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.time=ALL-UNNAMED add on override or ecache
class MyvopiServerApplication

fun main(args: Array<String>) {
    runApplication<MyvopiServerApplication>(*args)
}
