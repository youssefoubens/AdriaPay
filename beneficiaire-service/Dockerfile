FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the jar from target directory
COPY ./target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
