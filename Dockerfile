
FROM --platform=linux/amd64 openjdk:17-jdk-alpine AS builder

  # Set the working directory inside the container
WORKDIR /app

  # Copy the built JAR file into the container
  # Make sure you update `your-application.jar` to the actual JAR file name generated by your build (e.g., `target/myapp-0.0.1-SNAPSHOT.jar`)
COPY target/Fetch-0.0.1-SNAPSHOT.jar app.jar

  # Expose the port your Spring Boot application runs on
EXPOSE 8080

  # Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
