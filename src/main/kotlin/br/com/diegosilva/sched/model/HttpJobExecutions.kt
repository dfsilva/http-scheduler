package br.com.diegosilva.sched.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("http_job_executions")
data class HttpJobExecutions(@Id var id: Long?,
                             val jobId: String,
                             val dateTime: LocalDateTime,
                             val result: String = "",
                             val error: Boolean = false) {


}