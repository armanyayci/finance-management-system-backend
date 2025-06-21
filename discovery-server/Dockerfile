FROM amazoncorretto:17-alpine
WORKDIR /app
COPY ./target/*jar discovery-server.jar
ENTRYPOINT ["java", "-jar", "discovery-server.jar"]