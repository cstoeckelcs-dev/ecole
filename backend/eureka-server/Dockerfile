# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY eureka-server/pom.xml /app/eureka-server/pom.xml
RUN mvn -f /app/eureka-server/pom.xml dependency:go-offline -B || true
COPY eureka-server/src /app/eureka-server/src
RUN mvn -f /app/eureka-server/pom.xml package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/eureka-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
