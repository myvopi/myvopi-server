plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("kapt") version "1.7.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Spring security
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // MySQL driver
    runtimeOnly("com.mysql:mysql-connector-j")

//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    implementation("com.querydsl:querydsl-sql:5.0.0")
    implementation("com.querydsl:querydsl-sql-spring:5.0.0")

    // Gson
    implementation("com.google.code.gson:gson:2.8.9")

    // Json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Mapstruct
    implementation("org.mapstruct:mapstruct:1.5.2.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")

    // Mail Sender
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Base64
    implementation("commons-codec:commons-codec:1.15")
}

allOpen{
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
