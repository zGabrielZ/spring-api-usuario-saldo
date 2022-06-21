FROM openjdk:11
LABEL maintainer="Gabriel Ferreira <ferreiragabriel2612@gmail.coim>"
WORKDIR /app
EXPOSE 8080
COPY target/spring-usuarios.jar /app/spring-usuarios-api.jar
ENTRYPOINT ["java","-jar","spring-usuarios-api.jar"]