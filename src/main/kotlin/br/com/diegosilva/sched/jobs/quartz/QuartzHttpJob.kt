package br.com.diegosilva.sched.jobs.quartz

import br.com.diegosilva.sched.service.SchedulerService
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean

class QuartzHttpJob(
    val schedulerService: SchedulerService
) : QuartzJobBean() {

    private val log = LoggerFactory.getLogger(QuartzHttpJob::class.java)

    override fun executeInternal(context: JobExecutionContext) {
        context.jobDetail.jobDataMap?.let {
            it["jobId"]?.let { jobId -> schedulerService.runJob("$jobId") }
        }
    }


}