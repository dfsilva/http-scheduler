package br.com.diegosilva.sched.service

import br.com.diegosilva.sched.jobs.executors.HttpJobExecutor
import br.com.diegosilva.sched.jobs.quartz.QuartzHttpJob
import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.model.HttpJobExecution
import br.com.diegosilva.sched.model.HttpLastJobExecution
import br.com.diegosilva.sched.model.HttpRunningJob
import br.com.diegosilva.sched.repository.HttpJobDetailRepository
import br.com.diegosilva.sched.repository.HttpJobExecutionRepository
import br.com.diegosilva.sched.repository.HttpLastJobExecutionRepository
import br.com.diegosilva.sched.repository.HttpRunningJobRepository
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
    val httpJobExecutionsRepository: HttpJobExecutionRepository,
    val httpRunningJobRepository: HttpRunningJobRepository,
    val httpLastJobExecutionRepository: HttpLastJobExecutionRepository,
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
//                .requestRecovery(true)
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
        httpLastJobExecutionRepository.getErrorsIds().forEach {
            log.debug("Reesecutando $it")
            runJob(it)
        }

        httpLastJobExecutionRepository.getRunningIds().forEach {
            log.debug("Reesecutando Running $it")
            runJob(it)
        }
    }

    fun runJob(jobId: String) {
        httpJobDetailRepository.findById(jobId).ifPresent { httpJob ->
            if (!httpRunningJobRepository.findById(httpJob.jobId).isPresent) {

                httpRunningJobRepository.save(HttpRunningJob(jobId, LocalDateTime.now()))

                val lastJobExecution = httpLastJobExecutionRepository.findById(httpJob.jobId).map {
                    httpLastJobExecutionRepository.save(
                        HttpLastJobExecution.toUpdate(
                            it.copy(
                                status = "running",
                                dateTime = LocalDateTime.now(),
                                result = null
                            )
                        )
                    )
                }.orElseGet {
                    httpLastJobExecutionRepository.save(HttpLastJobExecution(jobId))
                }

                val jobExecution = httpJobExecutionsRepository.save(
                    HttpJobExecution(
                        jobId = httpJob.jobId,
                        status = "running",
                        dateTime = LocalDateTime.now()
                    )
                )

                log.debug("Vai chamar o SPRINGBATCH para executar a job $jobId")

                try {
                    val result = httpJobExecutor.execute(httpJob)
                    httpRunningJobRepository.deleteById(httpJob.jobId)
                    httpJobExecutionsRepository.save(jobExecution.copy(status = "success", result = result))
                    httpLastJobExecutionRepository.save(
                        HttpLastJobExecution.toUpdate(
                            lastJobExecution.copy(
                                result = result,
                                status = "success"
                            )
                        )
                    )
                } catch (ex: Exception) {
                    httpRunningJobRepository.deleteById(httpJob.jobId)
                    httpJobExecutionsRepository.save(jobExecution.copy(status = "error", result = ex.message))
                    httpLastJobExecutionRepository.save(
                        HttpLastJobExecution.toUpdate(
                            lastJobExecution.copy(
                                result = ex.message,
                                status = "error"
                            )
                        )
                    )
                }
                log.debug("Executou SPRINTBATCH para a job $jobId ")
            }
        }
    }
}