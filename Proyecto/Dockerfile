# Usa una imagen base de Java 17 con JDK
FROM openjdk:21-jdk-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el .jar generado en target
COPY target/Proyecto-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando de arranque, Spring Boot leerá las variables desde el entorno
ENTRYPOINT ["java", "-jar", "app.jar"]

