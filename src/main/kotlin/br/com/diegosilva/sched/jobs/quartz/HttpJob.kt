package br.com.diegosilva.sched.jobs.quartz

import br.com.diegosilva.sched.jobs.executors.HttpJobExecutor
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import java.time.Instant

class HttpJob(private val jobLauncher: JobLauncher, private val processJob: Job) : QuartzJobBean() {

    private val log = LoggerFactory.getLogger(HttpJob::class.java)

    override fun executeInternal(context: JobExecutionContext) {
        context.jobDetail.jobDataMap?.let {
            it["jobId"]?.let { jobId ->
                val jobParameters  = JobParametersBuilder()
                        .addString("jobId", jobId.toString())
                        .addLong("timestamp", Instant.now().epochSecond)
                        .toJobParameters()
                log.debug("Vai chamar o springbatch para executar a job ${jobId.toString()}")
                jobLauncher.run(processJob, jobParameters)
                log.debug("Executou springbatch para a job ${jobId.toString()}")
            }
        }
    }
}