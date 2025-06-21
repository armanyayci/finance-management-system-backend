FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*.jar currency-exchange-service.jar
ENTRYPOINT ["java", "-jar", "currency-exchange-service.jar"]