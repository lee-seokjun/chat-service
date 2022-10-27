FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/chat-service-0.0.1-SNAPSHOT.jar ChatService.jar
ENTRYPOINT ["java","-jar","ChatService.jar"]