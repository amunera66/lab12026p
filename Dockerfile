# Etapa 1: compilar el jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# Etapa 2: imagen de ejecucion
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/lab12026p.jar lab12026p.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","lab12026p.jar"]
