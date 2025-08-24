FROM maven:3.9-amazoncorretto-21 AS build

WORKDIR /app

COPY ms-client/pom.xml ./pom.xml
COPY ms-client/src ./src


# Empaquetamos la aplicación
RUN mvn package -DskipTests

# Etapa de ejecución
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/ms-client-1.0-SNAPSHOT.jar /app/


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ms-client-1.0-SNAPSHOT.jar"]