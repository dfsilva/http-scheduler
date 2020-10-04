package br.com.diegosilva.sched.jobs.batch

import br.com.diegosilva.sched.repository.JobDetailRepository
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import java.lang.RuntimeException

open class JobDetailTasklet(val jobId: String, val jobDetailRepository: JobDetailRepository) : Tasklet{
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        println("tasklet ${jobId}")
        throw RuntimeException("fasdfasdfas")
        return RepeatStatus.FINISHED;
    }

}