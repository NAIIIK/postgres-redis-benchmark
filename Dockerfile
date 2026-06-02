FROM eclipse-temurin:21-jre-alpine

WORKDIR /final4

COPY target/com.javarush.rodionov.hibernate-1.0-SNAPSHOT.jar final4.jar

ENTRYPOINT ["java", "-jar", "final4.jar"]