# "Build" stage
FROM maven:3-openjdk-11-slim AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

# "Run" stage
FROM openjdk:11-jre-slim
RUN addgroup eureka-server && adduser --no-create-home --disabled-password --ingroup eureka-server eureka-server
COPY --from=build /home/app/target/*.jar application.jar
ENV SPRING_PROFILES_ACTIVE=prod
CMD ["java", "-jar", "application.jar"]
EXPOSE 8761