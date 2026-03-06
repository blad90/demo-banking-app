FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copies parent POM
COPY pom.xml ./pom.xml

# Copy ONLY module POMs first (for dependency cache)
COPY transaction-saga-orchestrator/pom.xml transaction-saga-orchestrator/pom.xml
COPY services/api-gateway/pom.xml services/api-gateway/pom.xml
COPY services/user-service/pom.xml services/user-service/pom.xml
COPY services/account-service/pom.xml services/account-service/pom.xml
COPY services/transaction-service/pom.xml services/transaction-service/pom.xml
COPY services/fraud-screening/pom.xml services/fraud-screening/pom.xml

# Download dependencies (fast rebuilds)
RUN mvn -B -q -pl transaction-saga-orchestrator -am dependency:go-offline

# Copy full source code
COPY transaction-saga-orchestrator transaction-saga-orchestrator
COPY services services

# Building ONLY the saga orchestrator (but compile deps)
RUN mvn -pl transaction-saga-orchestrator -am clean package -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app

COPY --from=build /app/transaction-saga-orchestrator/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]