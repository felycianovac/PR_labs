FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8000 8001 8002 3000 3001 3002

ENTRYPOINT ["java", "-Draft.udp.port=${RAFT_UDP_PORT}", "-jar", "app.jar", "--server.port=${SERVER_PORT}"]
