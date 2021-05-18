package br.com.diegosilva.sched.jobs.batch

import br.com.diegosilva.sched.jobs.executors.HttpJobExecutor
import br.com.diegosilva.sched.repository.JobDetailRepository
import br.com.diegosilva.sched.repository.JobExecutionsRepository
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.retry.annotation.Retryable

open class JobDetailTasklet(val jobId: String,
                            val jobDetailRepository: JobDetailRepository,
                            val jobExecutionsRepository: JobExecutionsRepository,
                            val httpJobExecutor: HttpJobExecutor) : Tasklet{

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
       val httpJob = jobDetailRepository.findById(jobId)
        httpJob.ifPresent {
            httpJobExecutor.execute(it)
        }
        return RepeatStatus.FINISHED
    }

}