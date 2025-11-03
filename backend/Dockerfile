# =========================
# Queens Puzzle - Spring Boot Backend
# =========================

# ===== Stage 1: Build the JAR =====
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# ===== Stage 2: Run the JAR =====
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
