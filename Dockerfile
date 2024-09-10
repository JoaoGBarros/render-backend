FROM maven:3.8.5-jdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/seu-aplicativo.jar /app/seu-aplicativo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/seu-aplicativo.jar"]