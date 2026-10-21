
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/app.jar app.jar

EXPOSE 7070

ENTRYPOINT ["java", "-jar", "app.jar"]
