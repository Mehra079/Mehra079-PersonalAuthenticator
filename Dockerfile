FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "backend.jar"]
