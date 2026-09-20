# Build stage
FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /uploads
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "app.jar"]
