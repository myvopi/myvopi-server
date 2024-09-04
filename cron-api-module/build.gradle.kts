allOpen{
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.mysql:mysql-connector-j:9.0.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("com.querydsl:querydsl-sql-spring:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

//    implementation("org.mapstruct:mapstruct:1.5.2.Final")
//    kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")

    implementation("com.google.code.gson:gson:2.8.9")

    implementation("org.springframework.boot:spring-boot-starter-quartz:3.3.3")
    implementation("org.springframework:spring-context-support:6.1.12")
    implementation("org.springframework:spring-context:6.1.12")
}

tasks.named("build") {
    dependsOn("assemble")
}