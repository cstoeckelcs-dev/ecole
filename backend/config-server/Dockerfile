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
RUN mkdir -p /config
COPY --from=build /app/src/main/resources/config /config
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
