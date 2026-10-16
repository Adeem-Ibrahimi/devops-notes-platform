# ---- Stage 1: build ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true
COPY src src
RUN ./gradlew --no-daemon bootJar -x test

# ---- Stage 2: runtime ----
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -D -S -G appgroup appuser
WORKDIR /app
COPY --from=build --chown=appuser:appgroup /app/build/libs/task3-notes-api-0.0.1-SNAPSHOT.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
