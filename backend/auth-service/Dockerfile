# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY auth-service/pom.xml /app/auth-service/pom.xml
RUN mvn -f /app/auth-service/pom.xml dependency:go-offline -B || true
COPY auth-service/src /app/auth-service/src
RUN mvn -f /app/auth-service/pom.xml package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]
