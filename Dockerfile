FROM openjdk:8

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y upgrade

USER root

RUN mkdir code
COPY . /code

RUN cd /code && ./mvnw clean install -Dmaven.test.skip=true

EXPOSE 8080

ENTRYPOINT ["java", "-jar","/code/target/app.jar"]
