FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/account-service-1.0-dev-financemgmt.jar account-service.jar
ENTRYPOINT ["java", "-jar", "account-service.jar"]
