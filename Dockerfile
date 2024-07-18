FROM openjdk:17.0.1-oraclelinux8
COPY build/libs/*.jar hotsix.jar
ENTRYPOINT ["java", "-jar", "hotsix.jar"]
