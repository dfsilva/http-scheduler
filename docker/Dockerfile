FROM openjdk:11-jre
RUN mkdir app
ADD http-scheduler.jar /app/http-scheduler.jar
WORKDIR /app
ENTRYPOINT java -jar -Dspring.profiles.active=prod http-scheduler.jar