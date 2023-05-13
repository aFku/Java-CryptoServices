FROM openjdk:17.0.1-jdk-slim
COPY target/CryptoGenerator-0.0.1-SNAPSHOT.jar /usr/local/lib/CryptoGenerator.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dorg.slf4j.simpleLogger.defaultLogLevel=trace", "/usr/local/lib/CryptoGenerator.jar"]