FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /build/libs/*.jar app.jar  # ✅ wildcard
ENTRYPOINT ["java", "-jar", "app.jar"]