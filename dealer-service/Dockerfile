FROM openjdk:11.0-jdk
COPY "./target/dealer-service-0.0.1-SNAPSHOT.jar" "dealer-service.jar"
EXPOSE 8092
ENTRYPOINT [ "java","-jar","dealer-service.jar" ]
