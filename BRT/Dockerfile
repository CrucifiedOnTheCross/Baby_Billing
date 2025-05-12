FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/BRT-service.jar BRT-service.jar

ENTRYPOINT ["java", "-jar", "BRT-service.jar"]