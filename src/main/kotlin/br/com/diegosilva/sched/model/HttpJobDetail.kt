package br.com.diegosilva.sched.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("http_job_detail")
data class HttpJobDetail(@Id val jobId: String,
                         val description: String,
                         val cron: String,
                         val url: String,
                         val method: String,
                         val headerParams: String?,
                         val bodyParams: String?,
                         val queryParams: String?) : Persistable<String> {

    @Transient @JsonIgnore var isNewRow:Boolean = true

    override fun getId(): String? = jobId
    override fun isNew(): Boolean = isNewRow

    companion object{
        fun forUpdate(jobDetail: HttpJobDetail): HttpJobDetail {
            jobDetail.isNewRow = false
            return jobDetail
        }
    }
}
