FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

# Copies entire project
COPY . .

# Build only the transaction-service module
RUN mvn clean package -pl services/transaction-service -am -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /build/services/transaction-service/target/*jar transacion-service.jar
ENTRYPOINT ["java","-jar","/app/transaction-service.jar"]