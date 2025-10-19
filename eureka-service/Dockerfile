FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the jar from target directory
COPY ./target/eureka-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port (if your Spring Boot app runs on 8080)
EXPOSE 8761

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
