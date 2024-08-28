allOpen{
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    api(project(":common-core-module"))
    api(project(":external-api-module"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("io.micrometer:micrometer-tracing:1.3.3")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.3.3")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    runtimeOnly("com.mysql:mysql-connector-j:9.0.0")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("com.querydsl:querydsl-sql-spring:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    implementation("org.mapstruct:mapstruct:1.5.2.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")

    implementation("com.google.code.gson:gson:2.8.9")

    implementation("org.ehcache:ehcache:3.6.2")
    implementation("javax.cache:cache-api:1.1.0")
    implementation("org.springframework.boot:spring-boot-starter-cache")
}

tasks.named("build") {
    dependsOn("assemble")
}