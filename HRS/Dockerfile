FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/HRS-service.jar HRS-service.jar

ENTRYPOINT ["java", "-jar", "HRS-service.jar"]