# Multi-stage build for Java application
FROM maven:3.8.6-openjdk-11-slim AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application and copy dependencies
RUN mvn clean package -DskipTests
RUN mvn dependency:copy-dependencies -DoutputDirectory=target/lib

# Runtime stage
FROM eclipse-temurin:11-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built classes and all dependencies from builder stage
COPY --from=builder /app/target/classes /app/classes
COPY --from=builder /app/target/lib /app/lib

# Copy resources directory for configuration
COPY src/main/resources ./resources

# Create directory for sensor data if needed
RUN mkdir -p /app/data/sensors/input /app/data/sensors/output

# Expose port if application uses one (adjust if needed)
EXPOSE 8080

# Set environment variables (can be overridden in docker-compose)
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application with explicit classpath including all dependencies
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp 'classes:lib/*' App"]
