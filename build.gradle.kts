import java.io.BufferedWriter
import java.io.FileWriter
import java.util.Properties

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("kapt") version "1.7.22"
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.kapt")

    repositories {
        mavenCentral()
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    sourceSets {
        main {
            java.srcDir("src/main/kotlin")
        }
    }
}

subprojects {
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    }
}

val absolutePath: String = project.projectDir.absolutePath

private fun getEnvProperties(): Map<String, String> {
    val envFile = File("$absolutePath/env.properties")
        .inputStream()
    val properties = Properties()
    properties.load(envFile)
    return properties.stringPropertyNames()
        .associateWith { key -> properties.getProperty(key) }
}

private fun createNewEnvFile(directory: String, properties: Map<String, String>) {
    println("Creating env file at : $directory")
    try {
        val newEnv = File("$directory\\env.properties")
        if(newEnv.exists()) {
            newEnv.createNewFile()
        }
        val fw = FileWriter(newEnv)
        val w = BufferedWriter(fw)
        properties.forEach { (key, value) ->
            w.append("$key=$value\n")
        }
        w.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun deleteEnvFile(directory: String) {
    try {
        println("Deleting env file at : $directory")
        val env = File("$directory\\env.properties")
        env.delete()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun validateDirectory(gradleCommand: String): List<String> {
    return when {
        gradleCommand.contains("user-module-api") -> {
            listOf("$absolutePath\\user-api-module\\src\\main\\resources")
        }
        gradleCommand.contains("admin-module-api") -> {
            listOf("$absolutePath\\admin-api-module\\src\\main\\resources")
        }
        gradleCommand.contains("cron-module-api") -> {
            listOf("$absolutePath\\cron-api-module\\src\\main\\resources")
        }
        else -> {
            listOf(
                "$absolutePath\\user-api-module\\src\\main\\resources",
                "$absolutePath\\admin-api-module\\src\\main\\resources",
                "$absolutePath\\cron-api-module\\src\\main\\resources",
            )
        }
    }
}

private fun getSystemOs(): String {
    val osName = System.getProperty("os.name").lowercase()
    return if(osName.contains("windows")) {
        "win"
    } else {
        "other"
    }
}

private fun validateProject(gradleCommand: String) {
    val directories = validateDirectory(gradleCommand)
    println("Executed command: $gradleCommand")
    if(gradleCommand.contains("build")) {
        val customArg: String by project
        println("Custom Arguments: $customArg")
        val rootProperties = getEnvProperties()
        val userProperties: MutableMap<String, String>
        when {
            gradleCommand.contains("user-api-module") -> {
                userProperties = mutableMapOf(
                    "OS_SET" to getSystemOs(),
                    "ACTIVE_PROFILE" to customArg,
                    "DDL" to rootProperties["DDL"]!!,
                    "CIPHER_SECRET" to rootProperties["CIPHER_SECRET"]!!,
                    "CIPHER_IV" to rootProperties["CIPHER_IV"]!!,
                    "EMAIL_USERNAME" to rootProperties["EMAIL_USERNAME"]!!,
                    "EMAIL_PASSWORD" to rootProperties["EMAIL_PASSWORD"]!!,
                    "SSL_OR_TLS" to rootProperties["SSL_OR_TLS"]!!,
                    "USER_SECURITY_KEY" to rootProperties["USER_SECURITY_KEY"]!!,
                    "PRIMARY_HIKARI_MAX_POOLSIZE" to rootProperties["PRIMARY_HIKARI_MAX_POOLSIZE"]!!,
                    "PRIMARY_HIKARI_MINIMUM_IDLE" to rootProperties["PRIMARY_HIKARI_MINIMUM_IDLE"]!!,
                    "PRIMARY_HIKARI_MAX_LIFETIME" to rootProperties["PRIMARY_HIKARI_MAX_LIFETIME"]!!,
                    "PRIMARY_HIKARI_INIT_SQL" to rootProperties["PRIMARY_HIKARI_INIT_SQL"]!!,
                    "PRIMARY_HIKARI_CONNECTION_TIMEOUT" to rootProperties["PRIMARY_HIKARI_CONNECTION_TIMEOUT"]!!,
                    "PRIMARY_HIKARI_IDLE_TIMEOUT" to rootProperties["PRIMARY_HIKARI_IDLE_TIMEOUT"]!!,
                    "PRIMARY_HIKARI_VALIDATION_TIMEOUT" to rootProperties["PRIMARY_HIKARI_VALIDATION_TIMEOUT"]!!,
                )
            }
            gradleCommand.contains("admin-api-module") -> {
                userProperties = mutableMapOf(
                    "OS_SET" to getSystemOs(),
                    "ACTIVE_PROFILE" to customArg,
                    "CIPHER_SECRET" to rootProperties["CIPHER_SECRET"]!!,
                    "CIPHER_IV" to rootProperties["CIPHER_IV"]!!,
                    "ADMIN_SECURITY_KEY" to rootProperties["ADMIN_SECURITY_KEY"]!!,
                    "ADMIN_HIKARI_MAX_POOLSIZE" to rootProperties["ADMIN_HIKARI_MAX_POOLSIZE"]!!,
                    "ADMIN_HIKARI_MINIMUM_IDLE" to rootProperties["ADMIN_HIKARI_MINIMUM_IDLE"]!!,
                    "ADMIN_HIKARI_MAX_LIFETIME" to rootProperties["ADMIN_HIKARI_MAX_LIFETIME"]!!,
                    "ADMIN_HIKARI_INIT_SQL" to rootProperties["ADMIN_HIKARI_INIT_SQL"]!!,
                    "ADMIN_HIKARI_CONNECTION_TIMEOUT" to rootProperties["ADMIN_HIKARI_CONNECTION_TIMEOUT"]!!,
                    "ADMIN_HIKARI_IDLE_TIMEOUT" to rootProperties["ADMIN_HIKARI_IDLE_TIMEOUT"]!!,
                    "ADMIN_HIKARI_VALIDATION_TIMEOUT" to rootProperties["ADMIN_HIKARI_VALIDATION_TIMEOUT"]!!,
                )
            }
            gradleCommand.contains("cron-api-module") -> {
                userProperties = mutableMapOf(
                    "CRON_HIKARI_MAX_POOLSIZE" to rootProperties["CRON_HIKARI_MAX_POOLSIZE"]!!,
                    "CRON_HIKARI_MINIMUM_IDLE" to rootProperties["CRON_HIKARI_MINIMUM_IDLE"]!!,
                    "CRON_HIKARI_MAX_LIFETIME" to rootProperties["CRON_HIKARI_MAX_LIFETIME"]!!,
                    "CRON_HIKARI_INIT_SQL" to rootProperties["CRON_HIKARI_INIT_SQL"]!!,
                    "CRON_HIKARI_CONNECTION_TIMEOUT" to rootProperties["CRON_HIKARI_CONNECTION_TIMEOUT"]!!,
                    "CRON_HIKARI_IDLE_TIMEOUT" to rootProperties["CRON_HIKARI_IDLE_TIMEOUT"]!!,
                    "CRON_HIKARI_VALIDATION_TIMEOUT" to rootProperties["CRON_HIKARI_VALIDATION_TIMEOUT"]!!,
                )
            }
            else -> { throw Exception("Not a valid project") }
        }
        userProperties
            .apply {
                put("SOURCE_DIR", rootProperties["SOURCE_DIR"]!!)
                put("APP_NAME", rootProperties["APP_NAME"]!!)
                put("DATABASE_NAME", rootProperties["DATABASE_NAME"]!!)
            }
        if(customArg == "prod") {
            userProperties
                .apply {
                    put("PROD_DB_URL", rootProperties["PROD_DB_URL"]!!)
                    put("PROD_DB_USER", rootProperties["PROD_DB_USER"]!!)
                    put("PROD_DB_PASSWORD", rootProperties["PROD_DB_PASSWORD"]!!)
                }
        } else {
            userProperties
                .apply {
                    put("DEV_DB_URL", rootProperties["DEV_DB_URL"]!!)
                    put("DEV_DB_USER", rootProperties["DEV_DB_USER"]!!)
                    put("DEV_DB_PASSWORD", rootProperties["DEV_DB_PASSWORD"]!!)
                }
        }
        createNewEnvFile(directories.first(), userProperties)
    } else if(gradleCommand.contains("clean")) {
        directories.forEach { directory ->
            deleteEnvFile(directory)
        }
    } else {
        throw Exception("Not a valid command")
    }
}

tasks.getByName("build") {
    try {
        val commandName = this.project.gradle.startParameter.taskNames.first()
        validateProject(commandName)
    } catch (e: Exception) {}
}

project(":user-api-module") {
    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
    }

    tasks.getByName<Jar>("jar") {
        enabled = false
    }
}

project(":admin-api-module") {
    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
    }

    tasks.getByName<Jar>("jar") {
        enabled = false
    }
}

project(":common-core-module") {
    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = false
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}

project(":cron-api-module") {
    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
    }

    tasks.getByName<Jar>("jar") {
        enabled = false
    }
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}
