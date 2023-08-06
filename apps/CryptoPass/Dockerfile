FROM openjdk:17.0.1-jdk-slim
COPY target/CryptoPass-0.0.1-SNAPSHOT.jar /usr/local/lib/CryptoPass.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dorg.slf4j.simpleLogger.defaultLogLevel=trace", "/usr/local/lib/CryptoPass.jar"]