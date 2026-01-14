FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copies parent POM
COPY pom.xml /app/parent-pom.xml

# Installs ONLY the parent POM (no modules)
RUN mvn -f /app/parent-pom.xml -N clean install -DskipTests

# Copies service code
COPY services/account-service/pom.xml ./pom.xml
COPY services/account-service/src ./src

# Builds the service
RUN mvn clean package -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar account-service.jar
ENTRYPOINT ["java","-jar","/app/account-service.jar"]
