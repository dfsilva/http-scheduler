package br.com.diegosilva.sched.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("http_job_executions")
data class HttpJobExecutions(@Id var id: Long?,
                             val jobId: String,
                             val dateTime: Date,
                             val result: String,
                             val error: Boolean = false) {


}