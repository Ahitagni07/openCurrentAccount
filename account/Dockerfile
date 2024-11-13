# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /accountapp

# Copy the Maven project files and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project and build the application
COPY . .
RUN mvn clean package

# Stage 2: Create a smaller runtime image with only the JAR file
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /accountapp

# Copy the JAR file from the build stage
COPY --from=build /accountapp/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
