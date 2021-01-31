# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Pprod

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/rest-core-1.0.jar /usr/local/lib/rest-core-1.0.jar
EXPOSE 8080
ENV api.datasource.url=jdbc:postgresql://database-1.cruveyaqgg5i.eu-west-1.rds.amazonaws.com:5432/checker_db
ENV api.datasource.username=postgres
ENV api.datasource.password=checker_pass
ENTRYPOINT ["java","-jar","/usr/local/lib/rest-core-1.0.jar"]
