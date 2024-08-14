package com.example.myvopiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MyvopiServerApplication

fun main(args: Array<String>) {
    runApplication<MyvopiServerApplication>(*args)
}
