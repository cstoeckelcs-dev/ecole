# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY config-server/pom.xml /app/config-server/pom.xml
RUN mvn -f /app/config-server/pom.xml dependency:go-offline -B || true
COPY config-server/src /app/config-server/src
RUN mvn -f /app/config-server/pom.xml package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/config-server/target/*.jar app.jar
RUN mkdir -p /config
COPY --from=build /app/config-server/src/main/resources/config /config
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
