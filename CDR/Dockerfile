FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/CDR-service.jar CDR-service.jar

ENTRYPOINT ["java", "-jar", "CDR-service.jar"]