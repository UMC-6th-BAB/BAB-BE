FROM openjdk:17-oracle

WORKDIR /usr/src/app

ENV GOOGLE_APPLICATION_CREDENTIALS=/gcs-key.json

COPY /home/ubuntu/google/gcs-key.json /gcs-key.json

COPY build/libs/demo-0.0.1-SNAPSHOT.jar bab-snapshot.jar

ENTRYPOINT ["java", "-jar", "bab-snapshot.jar"]

