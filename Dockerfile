FROM openjdk:17.0.2-slim
COPY target/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]