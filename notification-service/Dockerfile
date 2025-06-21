FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*jar notification-service.jar
ENTRYPOINT ["java", "-jar", "notification-service.jar"]