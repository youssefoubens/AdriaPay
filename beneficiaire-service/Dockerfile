from openjdk:21-slim


WORKDIR  /app

COPY ./target/beneficiaire-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8071


ENTRYPOINT ["java","-jar","app.jar"]