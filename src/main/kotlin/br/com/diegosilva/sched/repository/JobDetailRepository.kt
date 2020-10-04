package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpJobDetail
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface JobDetailRepository: CrudRepository<HttpJobDetail, String> {

//
//    @Query("insert into http_sched.http_job_detail values()")
//    fun insert(@Param("detail") detail: HttpJobDetail)

}