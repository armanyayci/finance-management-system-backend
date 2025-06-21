FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*jar report-service.jar
ENTRYPOINT ["java", "-jar", "report-service.jar"]