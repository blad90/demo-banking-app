FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

# Copies entire project
COPY . .

# Build only the account-service module
RUN mvn clean package -pl services/account-service -am -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /build/services/account-service/target/*jar account-service.jar
ENTRYPOINT ["java","-jar","/app/account-service.jar"]