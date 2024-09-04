package com.example.cronserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
//@EnableJpaAuditing
// TODO need scheduler for reseting email verifications, video topic creation chance
class CronApiServerApplication

fun main(args: Array<String>) {
    runApplication<CronApiServerApplication>(*args)
}
