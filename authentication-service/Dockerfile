FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*.jar authentication-service.jar
ENTRYPOINT ["java", "-jar", "authentication-service.jar"]