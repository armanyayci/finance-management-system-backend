FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*jar user-service.jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]