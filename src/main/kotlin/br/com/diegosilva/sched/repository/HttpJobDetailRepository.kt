package br.com.diegosilva.sched.repository

import br.com.diegosilva.sched.model.HttpJobDetail
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HttpJobDetailRepository : CrudRepository<HttpJobDetail, String>