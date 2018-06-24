#build with sudo docker build . -t myeurekaserver
#run with sudo docker run myeukaserver

FROM java:8 

# Install maven
RUN apt-get update
RUN apt-get install -y maven

WORKDIR /

# Prepare by downloading dependencies
ADD pom.xml pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src src
RUN ["mvn", "package"]

#this is most likely the port
EXPOSE 8761
CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/MyEurekaServer-0.0.1-SNAPSHOT.jar"]
