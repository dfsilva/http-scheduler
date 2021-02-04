#!/bin/bash
docker run \
  --name http-scheduler \
  -e JDBC_URL="jdbc:postgresql://database_host:5432/postgres?currentSchema=http_sched" \
  -e JDBC_USER="postgres" \
  -e JDBC_PASSWORD="postgres" \
  -e INITIALIZE_SCHEMA="never" \
  -d \
  -p 8181:8181 \
  diego/http-scheduler

