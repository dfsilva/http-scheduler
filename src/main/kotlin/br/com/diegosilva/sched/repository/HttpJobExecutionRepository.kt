package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpJobExecution
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HttpJobExecutionRepository : CrudRepository<HttpJobExecution, Long> {

    @Query("select distinct job_id from http_job_executions where status = 'error'")
    fun getErrorsIds(): List<String>

    @Query("select distinct job_id from http_job_executions where status = 'running'")
    fun getRunningIds(): List<String>

    @Query("select * from http_job_executions where job_id = :jobId")
    fun findByJobId(@Param("jobId") jobId: String): Optional<HttpJobExecution>
}