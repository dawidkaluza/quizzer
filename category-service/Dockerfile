# "Build" stage
FROM maven:3-openjdk-11-slim AS build
COPY src /src
COPY pom.xml pom.xml
RUN mvn -f pom.xml clean package

# "Run" stage
FROM openjdk:11-jre-slim
COPY --from=build /target/*.jar application.jar
CMD ["java", "-jar", "application.jar"]