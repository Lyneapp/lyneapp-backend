FROM openjdk:17-jdk
WORKDIR /app
COPY target/lyneapp-backend-0.0.1-SNAPSHOT.jar /app/lyneapp-backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "target/lyneapp-backend-0.0.1-SNAPSHOT.jar"]

