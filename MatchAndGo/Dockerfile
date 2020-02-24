FROM maven:3.6.3-jdk-11

COPY . /target
WORKDIR /target

RUN mvn clean package

ENTRYPOINT mvn spring-boot:run
