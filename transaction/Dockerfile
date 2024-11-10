# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file (built by Maven or Gradle) into the container
ADD target/*.jar transaction.jar

# Expose the port that the service runs on (e.g., 8082)
EXPOSE 8082

# Run the jar file
ENTRYPOINT ["java", "-jar", "transaction.jar"]