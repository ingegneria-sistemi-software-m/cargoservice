#### Dockerfile in project conwaygui
FROM eclipse-temurin:17.0.5_8-jre-focal as builder
## ADD extracts the tar
ADD ./build/distributions/webgui-boot-0.0.1-SNAPSHOT.tar /
WORKDIR /webgui-boot-0.0.1-SNAPSHOT/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.bin ./
COPY ./*.txt ./
RUN mkdir logs

CMD ["bash", "webgui"]


## 1) gradlew distTar

## docker rmi conwayguialone:2.0   	 
## docker  build -t conwayguialone:2.0 .   //La version 2.0 ha ip local
## docker run -it --rm --name conwayguialone -p7110:7110 --env "HOST_IP=192.168.1.132" conwayguialone:2.0   
## docker run -it --rm --name conwayguialone -p7110:7110 --env "HOST_IP=192.168.1.34" conwayguialone:2.0  

## Register the image on DockerHub
## docker tag conwayguialone:2.0 natbodocker/conwayguialone:2.0
## type docker_password.txt | docker login --username natbodocker --password-stdin
## docker login --username natbodocker
## docker push natbodocker/conwayguialone:2.0

 