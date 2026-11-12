# Stage 1: Build the Spring Boot application with Java 21 and Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy dependency definition first to improve Docker layer caching
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code and build the executable JAR
COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Run the application on a lightweight Java 21 runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Render provides PORT at runtime; Spring Boot reads it from application.properties
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
