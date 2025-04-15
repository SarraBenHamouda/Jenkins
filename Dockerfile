FROM openjdk:17
EXPOSE 8082
COPY target/Foyer-0.0.1-SNAPSHOT.jar Foyer.jar
ENTRYPOINT ["java", "-jar", "Foyer.jar"]




