package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpJobExecutions
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HttpJobExecutionsRepository : CrudRepository<HttpJobExecutions, Long> {

}