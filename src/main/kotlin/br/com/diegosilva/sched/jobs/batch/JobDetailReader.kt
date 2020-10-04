package br.com.diegosilva.sched.jobs.batch

import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.repository.JobDetailRepository
import org.springframework.batch.item.ItemReader
import org.springframework.data.repository.findByIdOrNull

open class JobDetailReader(val jobId: String, val jobDetailRepository: JobDetailRepository) : ItemReader<HttpJobDetail> {

    var jobRunned = false

    override fun read(): HttpJobDetail? {
        if(!jobRunned){
            jobRunned = true
            return jobDetailRepository.findByIdOrNull(jobId)
        }
        return null
    }
}