package br.com.diegosilva.sched.jobs.executors

import br.com.diegosilva.sched.http.HttpRequest
import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.model.HttpJobExecutions
import br.com.diegosilva.sched.repository.JobExecutionsRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.util.*


@Component
class HttpJobExecutor(val jobExecutionsRepository: JobExecutionsRepository) {

    private val log = LoggerFactory.getLogger(HttpJobExecutor::class.java)

    @Retryable(maxAttempts = 10, backoff = Backoff(5000))
    fun execute(item: HttpJobDetail) {
        try {
            log.debug("Executando job ${item.jobId}")

            val mapper = jacksonObjectMapper()
            val headerParams: Map<String, String>? = item.headerParams?.let {
                mapper.readValue(it)
            }
            val queryParams: Map<String, String>? = item.queryParams?.let {
                mapper.readValue(it)
            }

            val str = HttpRequest.request(
                urlStr = item.url,
                method = item.method,
                header = headerParams,
                params = queryParams,
                body = item.bodyParams
            )
            jobExecutionsRepository.save(
                HttpJobExecutions(
                    id = null,
                    jobId = item.jobId,
                    dateTime = Date(),
                    result = str
                )
            )
        } catch (e: Exception) {
            log.error("Job com erro ${item.jobId}")
            jobExecutionsRepository.save(
                HttpJobExecutions(
                    id = null,
                    jobId = item.jobId,
                    dateTime = Date(),
                    result = e.message.orEmpty(),
                    error = true
                )
            )
            throw e
        }
    }
}