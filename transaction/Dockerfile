# Use Maven with JDK 17 to build and run the application
FROM maven:3.8.5-openjdk-17

# Set the working directory inside the container
WORKDIR /transactionapp

# Copy only the pom.xml first and download dependencies to cache them
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application (will create target/*.jar)
RUN mvn clean package -DskipTests

# Expose the port the Spring Boot application runs on
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "target/*.jar"]