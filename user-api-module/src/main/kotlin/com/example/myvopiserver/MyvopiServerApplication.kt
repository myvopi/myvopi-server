package com.example.myvopiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
// TODO --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.time=ALL-UNNAMED add on override or ecache
// TODO need scheduler for reseting email verifications, video topic creation chance
// TODO content size validation for post comment or reply
// TODO user information modify, needs password authentication, and returns new set of access token time limit of 5 minutes
// TODO optional, only requesting from my client?
// TODO email dkim setting?
class MyvopiServerApplication

fun main(args: Array<String>) {
    runApplication<MyvopiServerApplication>(*args)
}
