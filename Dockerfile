FROM openjdk:11
RUN useradd -ms /bin/bash sc


RUN mkdir -p /opt/testassigment
RUN chown sc:sc /opt/testassigment
USER sc

COPY assembly-local/testassigment-0.0.1-SNAPSHOT-spring-boot.jar /opt/testassigment
WORKDIR /opt/testassigment
CMD ["java","-jar","/opt/testassigment/testassigment-0.0.1-SNAPSHOT-spring-boot.jar"]