FROM adoptopenjdk:18-jdk-hotspot
EXPOSE 8080
ADD ./target/seguridad-0.0.1-SNAPSHOT.jar seguridad.jar
ENTRYPOINT ["java", "-jar", "/seguridad.jar"]
