# Use a base image with Java 17
FROM adoptopenjdk:17-jdk-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Copy the source code to the container
COPY src ./src

# Build the application with Maven and skip the tests
RUN mvn clean package -DskipTests

# Expose the port on which the application will run
EXPOSE 8082

# Set the entry point command to run the application
ENTRYPOINT ["java", "-jar", "target/lyneapp-backend-0.0.1-SNAPSHOT.jar"]

