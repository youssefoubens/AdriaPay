FROM openjdk:21-jdk-slim

# Installer curl pour les healthchecks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory inside container
WORKDIR /app

# Copy the jar from target directory
COPY ./target/*.jar app.jar



# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
