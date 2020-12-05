FROM openjdk:8

ADD target/cliente-microservice-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8090