# Use JDK 17 as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "target/mecatool-0.0.1-SNAPSHOT.jar"] 