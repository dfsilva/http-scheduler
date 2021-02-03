#!/bin/bash
docker run \
  --name http-scheduler \
  -e JDBC_URL="jdbc:postgresql://your_database:5432/postgres?currentSchema=http_sched" \
  -e JDBC_USER="postgres" \
  -e JDBC_PASSWORD="postgres" \
  -p 8181:8181 \
  -d \
  diego/http-scheduler

