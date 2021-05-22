package br.com.diegosilva.sched.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("http_last_job_executions")
data class HttpLastJobExecution(
    @Id val jobId: String,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val status: String = "running",
    val result: String? = null
) : Persistable<String> {

    @Transient @JsonIgnore var isNewRow:Boolean = true

    override fun getId(): String? = jobId
    override fun isNew(): Boolean = isNewRow


    companion object{
        fun toUpdate(jobDetail: HttpLastJobExecution): HttpLastJobExecution {
            jobDetail.isNewRow = false
            return jobDetail
        }
    }
}
