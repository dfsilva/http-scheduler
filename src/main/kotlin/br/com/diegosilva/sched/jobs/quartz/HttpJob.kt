package br.com.diegosilva.sched.jobs.quartz

import org.quartz.JobExecutionContext
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import java.time.Instant

class HttpJob(private val jobLauncher: JobLauncher, private val processJob: Job) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        context.jobDetail.jobDataMap?.let {
            it["jobId"]?.let { jobId ->
                val jobParameters  = JobParametersBuilder()
                        .addString("jobId", jobId.toString())
                        .addLong("timestamp", Instant.now().epochSecond)
                        .toJobParameters()
                jobLauncher.run(processJob, jobParameters)
            }
        }
    }
}