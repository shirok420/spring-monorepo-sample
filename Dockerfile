FROM eclipse-temurin:17-jdk as build

WORKDIR /app

# Copy gradle files first for better caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY core/build.gradle core/
COPY app/build.gradle app/

# Make gradlew executable
RUN chmod +x ./gradlew

# Download dependencies
RUN ./gradlew dependencies

# Copy source code
COPY core core
COPY app app

# Build the application
RUN ./gradlew app:bootJar

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built jar file from the build stage
COPY --from=build /app/app/build/libs/*.jar app.jar

# Create directory for SQLite database
RUN mkdir -p /data

# Set volume for persistent data
VOLUME /data
# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"


# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]