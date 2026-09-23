# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY school-service/pom.xml /app/school-service/pom.xml
RUN mvn -f /app/school-service/pom.xml dependency:go-offline -B || true
COPY school-service/src /app/school-service/src
RUN mvn -f /app/school-service/pom.xml package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/school-service/target/*.jar app.jar
RUN mkdir -p /uploads
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "app.jar"]
