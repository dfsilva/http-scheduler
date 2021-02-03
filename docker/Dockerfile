FROM openjdk:11-jre
RUN mkdir app
ADD /build/libs/http-scheduler-0.0.1-SNAPSHOT.jar /app/http-scheduler.jar
WORKDIR /app
ENTRYPOINT java -jar -Dspring.profiles.active=prod http-scheduler.jar