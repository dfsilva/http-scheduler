package br.com.diegosilva.sched.service

import br.com.diegosilva.sched.jobs.executors.HttpJobExecutor
import br.com.diegosilva.sched.jobs.quartz.QuartzHttpJob
import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.model.HttpJobExecutions
import br.com.diegosilva.sched.repository.HttpJobDetailRepository
import br.com.diegosilva.sched.repository.HttpJobExecutionsRepository
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.TriggerBuilder
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Service
class SchedulerService(
    val factory: SchedulerFactoryBean,
    val httpJobDetailRepository: HttpJobDetailRepository,
    val httpJobExecutionsRepository: HttpJobExecutionsRepository,
    val httpJobExecutor: HttpJobExecutor
) {

    private val log = LoggerFactory.getLogger(SchedulerService::class.java)


    fun scheduler(jobDetail: HttpJobDetail): CompletionStage<HttpJobDetail> {
        return CompletableFuture.supplyAsync {

            log.debug("Criando job ${jobDetail.jobId}")

            httpJobDetailRepository.save(jobDetail)

            val job = JobBuilder.newJob().ofType(QuartzHttpJob::class.java).storeDurably()
                .withIdentity(jobDetail.id)
                .withDescription(jobDetail.description)
                .usingJobData("jobId", jobDetail.jobId)
                .requestRecovery(true)
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
            httpJobDetailRepository.deleteById(jobId)
            factory.scheduler.deleteJob(JobKey.jobKey(jobId))
        }
    }

    fun runFailedJobs() {
        httpJobExecutionsRepository.getIdsWithErrors().forEach {
            log.debug("Reesecutando $it")
            runJob(it)
        }
    }

    fun runJob(jobId: String) {
        httpJobDetailRepository.findById(jobId).ifPresent { httpJob ->
            val jobExecution = httpJobExecutionsRepository.save(
                HttpJobExecutions(
                    jobId = httpJob.jobId,
                    status = "running",
                    dateTime = LocalDateTime.now(),
                    result = ""
                )
            )
            log.debug("Vai chamar o SPRINGBATCH para executar a job $jobId")
            try {
                val result = httpJobExecutor.execute(httpJob)
                httpJobExecutionsRepository.save(jobExecution.copy(status = "success", result = result))
            } catch (ex: Exception) {
                httpJobExecutionsRepository.save(jobExecution.copy(status = "error", result = ex.message))
            }
            log.debug("Executou SPRINTBATCH para a job $jobId ")
        }
    }
}