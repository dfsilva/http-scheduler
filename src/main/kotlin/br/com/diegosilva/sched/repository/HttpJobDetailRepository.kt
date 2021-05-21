package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpJobDetail
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HttpJobDetailRepository : CrudRepository<HttpJobDetail, String> {

    @Query("select distinct job_id from http_job_detail where status = 'error';")
    fun getIdsWithErrors(): List<String>

}