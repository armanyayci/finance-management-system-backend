FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*.jar config-service.jar
ENTRYPOINT ["java", "-jar", "config-service.jar"]