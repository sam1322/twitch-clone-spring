#FROM openjdk:17-jdk-alpine
#MAINTAINER baeldung.com
#COPY target/docker-message-server-1.0.0.jar message-server-1.0.0.jar
#ENTRYPOINT ["java","-jar","/message-server-1.0.0.jar"]


#FROM eclipse-temurin:17-jdk-focal
#FROM openjdk:17-jdk-alpine
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#
#COPY src ./src
#EXPOSE 8080
#
#CMD ["./mvnw", "spring-boot:run"]

#FROM openjdk:17-jdk-alpine
##FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#COPY target/*.jar app.jar
##ARG JAR_FILE
##COPY ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM maven:3.9.6-eclipse-temurin-17 AS maven

COPY src /tmp/twitch-clone/src
COPY pom.xml /tmp/twitch-clone
RUN mvn -f /tmp/twitch-clone/pom.xml -Dspring.profiles.active=dev clean install

FROM eclipse-temurin:17
RUN mkdir -p /val/log/twitch-clone
WORKDIR /opt/service/twitch-clone
COPY --from=maven /tmp/twitch-clone/src/main/resources/application-dev.properties /opt/service/twitch-clone/application-dev.properties
COPY --from=maven /tmp/twitch-clone/target/twitch-clone-0.0.1-SNAPSHOT.jar /opt/service/twitch-clone

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "twitch-clone-0.0.1-SNAPSHOT.jar"]
#ENV DATABASE_URL="jdbc:postgresql://localhost:5432/postgres?user=postgres&password=123456"
