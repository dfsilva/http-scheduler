package br.com.diegosilva.sched.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("http_job_executions")
data class HttpJobExecution(
    @Id var id: Long? = null,
    val jobId: String,
    val dateTime: LocalDateTime,
    val result: String? = null,
    val status: String
)