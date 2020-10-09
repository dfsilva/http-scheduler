package br.com.diegosilva.sched.executors

import br.com.diegosilva.sched.http.HttpRequest
import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.model.HttpJobExecutions
import br.com.diegosilva.sched.repository.JobExecutionsRepository

import org.springframework.stereotype.Component
import java.util.*

@Component
class HttpJobExecutor(val jobExecutionsRepository: JobExecutionsRepository) {
    fun execute(item: HttpJobDetail) {
        val str = HttpRequest.request(urlStr = item.url, method = item.method)
        jobExecutionsRepository.save(HttpJobExecutions(id = null, jobId = item.jobId, dateTime = Date(), result = str))
        print(str)
    }
}