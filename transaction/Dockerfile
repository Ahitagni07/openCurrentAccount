# Use Maven with JDK 17 to build and run the application
FROM maven:3.8.5-openjdk-17

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files into the container
COPY . .

# Package the application (will create target/*.jar)
RUN mvn clean package -DskipTests

# Expose the port the Spring Boot application runs on
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "target/*.jar"]