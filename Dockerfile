FROM openjdk:17
EXPOSE 8089
ADD target/Foyer-0.0.1-SNAPSHOT.jar Foyer.jar
ENTRYPOINT ["java", "-jar", "Foyer.jar"]



