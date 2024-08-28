package com.example.myvopiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
// TODO --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.time=ALL-UNNAMED add on override or ecache
// TODO need to check each api due to inefficient queries round backs
// TODO need scheduler for reseting email verifications, video topic creation chance
// TODO content size validation for post comment or reply
class MyvopiServerApplication

fun main(args: Array<String>) {
    runApplication<MyvopiServerApplication>(*args)
}
