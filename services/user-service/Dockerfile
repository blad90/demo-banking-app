FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy parent POM
COPY pom.xml /app/parent-pom.xml

# Install ONLY the parent POM (no modules)
RUN mvn -f /app/parent-pom.xml -N clean install -DskipTests

# Copy service code
COPY services/user-service/pom.xml ./pom.xml
COPY services/user-service/src ./src

# Build the service
RUN mvn clean package -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar user-service.jar
ENTRYPOINT ["java","-jar","/app/user-service.jar"]
