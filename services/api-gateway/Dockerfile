FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

# Copy parent pom ONLY
COPY ../../pom.xml ./pom.xml

# Install parent into local repo
RUN mvn -B -N clean install

# Copy the rest
COPY services ./services
COPY account-saga-orchestrator ./account-saga-orchestrator

# Build only the api-gateway module
RUN mvn clean package -pl services/api-gateway -am -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /build/services/api-gateway/target/*jar api-gateway.jar
ENTRYPOINT ["java","-jar","/app/api-gateway.jar"]