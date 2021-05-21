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
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Service
class SchedulerService(
    val factory: SchedulerFactoryBean,
    val httpJobDetailRepository: HttpJobDetailRepository,
    val httpJobExecutionRepository: HttpJobExecutionsRepository,
    val jobLauncher: JobLauncher,
    val jobFactory: JobBuilderFactory,
    val stepBuilder: StepBuilderFactory,
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

    fun runFailureJobs(jobId: String) {
        CompletableFuture.supplyAsync {

        }
    }

    fun runJob(jobId: String) {

        val jobExecutions = httpJobExecutionRepository.save(HttpJobExecutions(id = null, jobId = jobId, dateTime =  LocalDateTime.now()))

        val jobParameters = JobParametersBuilder()
            .addString("jobId", jobId)
            .addLong("timestamp", Instant.now().epochSecond)
            .toJobParameters()

        log.debug("Vai chamar o SPRINGBATCH para executar a job $jobId")

        val processJob = jobFactory
            .get(jobId)
            .incrementer(RunIdIncrementer())
            .start(stepBuilder
                .get("step-${jobId}")
                .tasklet { _, _ ->
                    httpJobDetailRepository.findById(jobId).ifPresent(httpJobExecutor::execute)
                    RepeatStatus.FINISHED
                }
                .build()).build()

        val jobExecution = jobLauncher.run(processJob, jobParameters)

        if(jobExecution.allFailureExceptions.isNotEmpty()){
            httpJobExecutionRepository.save(jobExecutions.copy(error = true))
        }

        log.debug(
            "Executou SPRINTBATCH para a job $jobId " +
                    "com retorno ${jobExecution.exitStatus.exitCode}" +
                    " com ${jobExecution.allFailureExceptions.size} erros"
        )
    }
}