# =========================
# Queens Puzzle - Spring Boot Backend
# =========================

# Stage 1: build JAR
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: run JAR
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Default Render port
ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
