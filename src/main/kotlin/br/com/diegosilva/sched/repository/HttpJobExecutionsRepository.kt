package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpJobExecutions
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HttpJobExecutionsRepository : CrudRepository<HttpJobExecutions, Long> {

    @Query("select distinct job_id from http_job_executions where status = 'error';")
    fun getIdsWithErrors(): List<String>
}