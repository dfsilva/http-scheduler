package br.com.diegosilva.sched.service

import br.com.diegosilva.sched.jobs.quartz.HttpJob
import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.repository.JobDetailRepository
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.TriggerBuilder
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Service
class SchedulerService(val factory: SchedulerFactoryBean, val jobDetailRepository: JobDetailRepository) {

    fun scheduler(jobDetail: HttpJobDetail): CompletionStage<HttpJobDetail> {
        return CompletableFuture.supplyAsync {
            jobDetailRepository.save(jobDetail)

            val job = JobBuilder.newJob().ofType(HttpJob::class.java).storeDurably()
                .withIdentity(jobDetail.id)
                .withDescription(jobDetail.description)
                .usingJobData("jobId", jobDetail.jobId)
                .build()

            val trigger = TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("trigger_${jobDetail.id}")
                .withDescription("Trigger ${jobDetail.description}")
                .withSchedule(cronSchedule(jobDetail.cron))
                .build()

            factory.scheduler.scheduleJob(job, trigger)

            jobDetail
        }
    }

    fun delete(jobId: String): CompletionStage<Boolean> {
        return CompletableFuture.supplyAsync {
            jobDetailRepository.deleteById(jobId)
            factory.scheduler.deleteJob(JobKey.jobKey(jobId))
        }
    }
}