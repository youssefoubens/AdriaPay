FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the jar from target directory
COPY ./target/Gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose port (if your Spring Boot app runs on 8080)
EXPOSE 8073

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
