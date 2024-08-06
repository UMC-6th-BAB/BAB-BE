FROM openjdk:17-oracle

WORKDIR /usr/src/app

COPY build/libs/demo-0.0.1-SNAPSHOT.jar bab-snapshot.jar

ENTRYPOINT ["java", "-jar", "bab-snapshot.jar"]

