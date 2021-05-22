create schema if not exists http_sched;

CREATE TABLE IF NOT EXISTS http_sched.http_job_detail
(
    job_id        varchar primary key,
    description   varchar not null,
    cron          varchar not null,
    url           varchar not null,
    method        varchar not null,
    header_params varchar null,
    body_params   varchar null,
    query_params  varchar null
);

CREATE TABLE IF NOT EXISTS http_sched.http_last_job_executions
(
    job_id     varchar primary key,
    date_time timestamp not null,
    status    varchar   not null,
    result    text      null
);

CREATE TABLE IF NOT EXISTS http_sched.http_job_executions
(
    id        serial primary key,
    job_id    varchar   not null,
    date_time timestamp not null,
    result    text      null,
    status    varchar   not null
);

CREATE TABLE IF NOT EXISTS http_sched.http_running_jobs
(
    job_id     varchar primary key,
    start_time timestamp not null
);