FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

# Copies entire project
COPY . .

# Build only the user-service module
RUN mvn clean package -pl services/user-service -am -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /build/services/user-service/target/*jar user-service.jar
ENTRYPOINT ["java","-jar","/app/user-service.jar"]
