FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/CRM-service.jar CRM-service.jar

ENTRYPOINT ["java", "-jar", "CRM-service.jar"]