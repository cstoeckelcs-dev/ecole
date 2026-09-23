# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY gateway/pom.xml /app/gateway/pom.xml
RUN mvn -f /app/gateway/pom.xml dependency:go-offline -B || true
COPY gateway/src /app/gateway/src
RUN mvn -f /app/gateway/pom.xml package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
