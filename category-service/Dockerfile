# "Build" stage
FROM maven:3-openjdk-11-slim AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# "Run" stage
FROM openjdk:11-jre-slim
RUN addgroup backend && adduser --no-create-home --disabled-password --ingroup backend backend
COPY dockerize .
COPY --from=build /home/app/target/*.jar application.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENV DATABASE_HOST=localhost
ENV DATABASE_PORT=5432
CMD ./dockerize -wait tcp://${DATABASE_HOST}:${DATABASE_PORT} -timeout 15s -wait-retry-interval 5s java -jar application.jar
EXPOSE 8200