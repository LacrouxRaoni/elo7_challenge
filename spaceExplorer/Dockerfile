FROM maven:3.8.6-eclipse-temurin-11 AS build

RUN mkdir /artefact
COPY . /artefact
WORKDIR /artefact
RUN mvn clean package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app
COPY --from=build /artefact/target/space-explorer.jar space-explorer.jar

ENTRYPOINT ["java","-jar","space-explorer.jar"]

EXPOSE 8080
