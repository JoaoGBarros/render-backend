# Usar uma imagem oficial do Java
FROM openjdk:17-jdk-alpine

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo JAR da aplicação para o container
COPY target/*.jar app.jar

# Expor a porta 8080
EXPOSE 8080

# Comando para rodar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
