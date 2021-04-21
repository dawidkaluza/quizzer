# Build stage
FROM maven:3-openjdk-11-slim AS build
COPY src /src
COPY pom.xml pom.xml
RUN mvn -f pom.xml clean package

# Package stage
FROM openjdk:11-jre-slim
COPY --from=build /target/eureka-server-0.0.1-SNAPSHOT.jar application.jar
CMD ["java", "-jar", "application.jar"]