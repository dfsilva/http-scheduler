package br.com.diegosilva.sched.jobs.executors

import br.com.diegosilva.sched.http.HttpRequest
import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.model.HttpJobExecutions
import br.com.diegosilva.sched.repository.JobExecutionsRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.util.*


@Component
class HttpJobExecutor(val jobExecutionsRepository: JobExecutionsRepository) {
    @Retryable(maxAttempts = 5)
    fun execute(item: HttpJobDetail) {
        try {
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
                body = item.bodyParams)
            jobExecutionsRepository.save(HttpJobExecutions(id = null, jobId = item.jobId, dateTime = Date(), result = str))
        }
        catch (e: Exception) {
            jobExecutionsRepository.save(HttpJobExecutions(id = null, jobId = item.jobId, dateTime = Date(), result = e.message.orEmpty(), error = true))
            throw e
        }
    }
}