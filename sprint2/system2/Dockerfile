## ----------------------------------------------------------
## Dockerfile for arch2
## ----------------------------------------------------------

FROM eclipse-temurin:17.0.5_8-jre-focal as builder
## EXPOSE 8020
## ADD extracts the tar
ADD ./build/distributions/arch1-2.0.tar /
WORKDIR /arch2-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.bin ./
COPY ./*.txt ./

## RUN apt-get update -y
## RUN apt-get install -y wiringpi
## RUN sudo apt-get install -y python

CMD ["bash", "arch2"]

## 0) docker rmi basicrobot24:3.0
## 1) gradlew distTar
## 2) docker build -t basicrobot24:3.0 .
## OCCORRE APRIRE UDP PER PERMETTERE COAP
## docker run -it --rm --name basicrobot24 -p8020:8020/tcp -p8020:8020/udp  --privileged basicrobot24:3.0  /bin/bash
## MODIFICA DEL FILE DI CONFIGURAZIONE
# docker cp basicrobotConfig.json 89193dba02a2:/basicrobot24-1.0/bin/basicrobotConfig.json
# docker cp stepTimeConfig.json cab3aa29f8eb:/basicrobot24-1.0/bin/stepTimeConfig.json

# docker tag basicrobot24:3.0 natbodocker/basicrobot24:3.0

#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
# docker push natbodocker/basicrobot24:3.0


# docker-compose -f basicrobot24.yaml  up