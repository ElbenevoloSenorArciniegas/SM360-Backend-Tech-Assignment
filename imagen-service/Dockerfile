FROM openjdk:11.0-jdk
COPY "./target/imagen-service-0.0.1-SNAPSHOT.jar" "imagen-service.jar"
EXPOSE 8092
ENTRYPOINT [ "java","-jar","imagen-service.jar" ]
