FROM openjdk:11.0-jdk
COPY "./target/gateway-service-0.0.1-SNAPSHOT.jar" "gateway-service.jar"
EXPOSE 8080
ENTRYPOINT [ "java","-jar","gateway-service.jar" ]
