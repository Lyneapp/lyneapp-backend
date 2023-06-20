FROM openjdk:17-jdk
WORKDIR /app
COPY target/lyneapp-backend-0.0.1-SNAPSHOT.jar /app/lyneapp-backend-0.0.1-SNAPSHOT.jar
EXPOSE 8081
CMD ["java", "-jar", "lyneapp-backend-0.0.1-SNAPSHOT.jar"]


