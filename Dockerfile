FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN chmod +x gradlew  # ✅ dá permissão de execução
RUN ./gradlew clean build -x test  # ✅ usa o wrapper do projeto

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /build/libs/cidasdepilacao-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
