# Use Maven with JDK 17 to build and run the application
FROM maven:3.8.5-openjdk-17

# Set the working directory inside the container
WORKDIR /accountapp

# Copy the Maven project files into the container
COPY . .

# Package the application (will create target/*.jar)
RUN mvn clean package

# Expose the port the Spring Boot application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]