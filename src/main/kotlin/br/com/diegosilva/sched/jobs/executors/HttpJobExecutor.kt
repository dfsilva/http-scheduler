package br.com.diegosilva.sched.jobs.executors

import br.com.diegosilva.sched.http.HttpRequest
import br.com.diegosilva.sched.model.HttpJobDetail
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component


@Component
class HttpJobExecutor {

    private val log = LoggerFactory.getLogger(HttpJobExecutor::class.java)

    @Retryable(
        maxAttemptsExpression = "\${retry.maxAttempts}",
        backoff = Backoff(delayExpression = "\${retry.backoff}", multiplier = 1.3, maxDelay = 100000)
    )
    fun execute(item: HttpJobDetail) {
        log.debug("Executando job ${item.jobId}")

        val mapper = jacksonObjectMapper()
        val headerParams: Map<String, String>? = item.headerParams?.let {
            mapper.readValue(it)
        }
        val queryParams: Map<String, String>? = item.queryParams?.let {
            mapper.readValue(it)
        }

        HttpRequest.request(
            urlStr = item.url,
            method = item.method,
            header = headerParams,
            params = queryParams,
            body = item.bodyParams
        )
    }
}