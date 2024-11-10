# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Copy the JAR file (built by Maven or Gradle) into the container
COPY ./target/*.jar account-service.jar

# Expose the port that the service runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/account-service.jar"]
