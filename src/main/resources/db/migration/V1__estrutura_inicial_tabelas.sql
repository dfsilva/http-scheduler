CREATE TABLE IF NOT EXISTS http_sched.http_job_detail
(
    job_id        varchar primary key,
    description   varchar not null,
    cron          varchar not null,
    url           varchar not null,
    header_params varchar null,
    body_params   varchar null,
    query_params  varchar null
)