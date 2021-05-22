package br.com.diegosilva.sched.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("http_running_jobs")
data class HttpRunningJob(
    @Id val jobId: String,
    val startTime: LocalDateTime
) : Persistable<String> {

    @Transient
    @JsonIgnore
    var isNewRow: Boolean = true

    override fun getId(): String? = jobId
    override fun isNew(): Boolean = isNewRow

}
