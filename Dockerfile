# ============================================
# Stage 1: Build
# ============================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy seluruh project
COPY . .

# Build aplikasi (skip test karena sudah dijalankan di CI)
RUN mvn clean package -DskipTests

# ============================================
# Stage 2: Runtime
# ============================================
FROM eclipse-temurin:17-jre-alpine

# Buat user non-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy file JAR hasil build
COPY --from=builder /app/target/*.jar app.jar

# Ubah ownership file
RUN chown appuser:appgroup app.jar

# Gunakan user non-root
USER appuser

# Port aplikasi
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# Jalankan aplikasi
ENTRYPOINT ["java","-jar","app.jar"]
