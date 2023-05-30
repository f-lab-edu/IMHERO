FROM openjdk:11-jre-slim

WORKDIR /home/ubuntu/

COPY /build/libs/imhero-0.0.1-SNAPSHOT.jar .

CMD java -jar imhero-0.0.1-SNAPSHOT.jar
