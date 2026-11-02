# --- Build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Baixa as dependências antes de copiar o código, aproveitando o cache de camadas
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests

# --- Runtime ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
