# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container
COPY . .

# Build the project
RUN ./gradlew build -x test

# Expose the port the application runs on
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "build/libs/assessment-0.0.1-SNAPSHOT.jar"]