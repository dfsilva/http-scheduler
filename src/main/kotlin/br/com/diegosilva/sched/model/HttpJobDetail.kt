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
                         val headerParams: String?,
                         val bodyParams: String?,
                         val queryParams: String?,
                         @Transient @JsonIgnore val isNewRow:Boolean = true) : Persistable<String> {


    override fun getId(): String? {
        return jobId
    }

    override fun isNew(): Boolean {
        return isNewRow
    }
}