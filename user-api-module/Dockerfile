FROM openjdk:17-jdk-alpine
WORKDIR /user-api-module
ARG JAR_FILE=/build/libs/*-SNAPSHOT.jar
ARG DOCKER_OUT_DIR=/src/main/kotlin/com/example/myvopiserver
ARG MAIN_DIR=src/main/kotlin/com/example/myvopiserver
ARG CLASS_DIR=${MAIN_DIR}/domain
ARG CONTROLLER_DIR=${MAIN_DIR}/interfaces

COPY ${JAR_FILE} /user-api-module/app.jar
COPY ${CLASS_DIR}/*.kt /user-api-module${DOCKER_OUT_DIR}/domain/
COPY ${CONTROLLER_DIR}/*.kt /user-api-module${DOCKER_OUT_DIR}/interfaces/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]