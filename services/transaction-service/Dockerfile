# ---------- BUILD STAGE ----------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy entire project (all modules)
COPY . .

# Build only transaction-service + required modules
RUN mvn -pl services/transaction-service -am clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM amazoncorretto:25-alpine
WORKDIR /app

COPY --from=build /app/services/transaction-service/target/*.jar transaction-service.jar

ENTRYPOINT ["java","-jar","/app/transaction-service.jar"]