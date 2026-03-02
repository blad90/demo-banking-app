# ---------- BUILD STAGE ----------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy entire project (all modules)
COPY . .

# Build only account-service + required modules
RUN mvn -pl services/account-service -am clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM amazoncorretto:25-alpine
WORKDIR /app

COPY --from=build /app/services/account-service/target/*.jar account-service.jar

ENTRYPOINT ["java","-jar","/app/account-service.jar"]