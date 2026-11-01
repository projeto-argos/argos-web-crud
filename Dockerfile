FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /argos-web-crud

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:11-jdk21

ENV TZ=America/Sao_Paulo \
    CATALINA_OPTS="-Xms256m -Xmx512m"

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=builder /argos-web-crud/target/Argos-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]