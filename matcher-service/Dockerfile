## Build stage
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /workspace

# Copy Gradle wrapper and build files first to leverage caching
COPY matcher-service/gradlew matcher-service/gradlew.bat matcher-service/settings.gradle matcher-service/build.gradle ./
COPY matcher-service/gradle ./gradle

# Copy source
COPY matcher-service/src ./src

# Build the application (only the bootable jar is needed)
RUN ./gradlew --no-daemon clean bootJar

## Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

