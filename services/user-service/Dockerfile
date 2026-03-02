# ---------- BUILD STAGE ----------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy entire project (all modules)
COPY . .

# Build only user-service + required modules
RUN mvn -pl services/user-service -am clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM amazoncorretto:25-alpine
WORKDIR /app

COPY --from=build /app/services/user-service/target/*.jar user-service.jar

ENTRYPOINT ["java","-jar","/app/user-service.jar"]