FROM openjdk:11.0-jdk AS registry
RUN mvn clean install

FROM openjdk:11.0-jdk
COPY "$registry/target/registry-service-0.0.1-SNAPSHOT.jar" "registry-service.jar"
EXPOSE 8099
ENTRYPOINT [ "java","-jar","registry-service.jar" ]
