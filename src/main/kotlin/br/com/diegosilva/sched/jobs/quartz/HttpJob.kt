package br.com.diegosilva.sched.jobs.quartz

import org.quartz.JobExecutionContext
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean

class HttpJob(val jobLauncher: JobLauncher, val processJob: Job) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        context.jobDetail.jobDataMap?.let {
            it["jobId"]?.let { jobId ->
                val params = JobParametersBuilder().addString("jobId", jobId.toString()).toJobParameters()
                jobLauncher.run(processJob, params)
            }
        }
    }
}