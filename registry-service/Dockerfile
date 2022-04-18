FROM openjdk:11.0-jdk
COPY "./target/registry-service-0.0.1-SNAPSHOT.jar" "registry-service.jar"
EXPOSE 8761
#ENTRYPOINT [ "java","-jar","registry-service.jar" ]

# Add docker-compose-wait tool -------------------
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait