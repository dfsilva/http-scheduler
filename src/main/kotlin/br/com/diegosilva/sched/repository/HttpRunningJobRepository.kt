package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpRunningJob
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HttpRunningJobRepository : CrudRepository<HttpRunningJob, String> {

}