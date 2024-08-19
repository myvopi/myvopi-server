dependencies {
    api(project(":common-core-module"))
    api(project(":entity-core-module"))

    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.google.code.gson:gson:2.8.9")
}