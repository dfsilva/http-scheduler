package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpLastJobExecution
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HttpLastJobExecutionRepository : CrudRepository<HttpLastJobExecution, String>{

    @Query("select distinct job_id from last_job_executions where status = 'error'")
    fun getErrorsIds(): List<String>


    @Query("select distinct job_id from last_job_executions where status = 'running'")
    fun getRunningIds(): List<String>
}