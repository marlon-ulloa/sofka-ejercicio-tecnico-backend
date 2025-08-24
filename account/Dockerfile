FROM maven:3.9-amazoncorretto-21 AS build

WORKDIR /app

COPY ms-account/pom.xml ./pom.xml
COPY ms-account/src ./src


# Empaquetamos la aplicación
RUN mvn package -DskipTests

# Etapa de ejecución
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copiamos el jar compilado
COPY --from=build /app/target/ms-account-1.0-SNAPSHOT.jar /app/


EXPOSE 8081
ENTRYPOINT ["java", "-jar", "ms-account-1.0-SNAPSHOT.jar"]