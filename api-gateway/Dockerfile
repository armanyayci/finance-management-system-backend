FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]