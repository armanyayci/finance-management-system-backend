FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*.jar mock-service.jar
ENTRYPOINT ["java", "-jar", "mock-service.jar"]