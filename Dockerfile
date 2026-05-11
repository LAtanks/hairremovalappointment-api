FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
<<<<<<< HEAD
COPY --from=build /build/libs/*.jar app.jar  # ✅ wildcard
ENTRYPOINT ["java", "-jar", "app.jar"]
=======
COPY --from=build /build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> a532a5e737a5194ffb6bcea5f17669729aa3dff4
