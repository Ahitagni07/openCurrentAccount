# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /transactionapp

# Copy the Maven project files and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project and build the application
COPY . .
RUN mvn clean package

# Stage 2: Create a smaller runtime image with only the JAR file
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /transactionapp

# Copy the JAR file from the build stage
COPY --from=build /transactionapp/target/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
