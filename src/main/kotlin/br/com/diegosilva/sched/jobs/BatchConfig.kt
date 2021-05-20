package br.com.diegosilva.sched.jobs

import br.com.diegosilva.sched.jobs.batch.JobDetailTasklet
import br.com.diegosilva.sched.jobs.executors.HttpJobExecutor
import br.com.diegosilva.sched.repository.JobDetailRepository
import br.com.diegosilva.sched.repository.JobExecutionsRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@Configuration
@EnableBatchProcessing
@EnableRetry
class BatchConfig(
    val jobFactory: JobBuilderFactory,
    val stepBuilder: StepBuilderFactory
) {
    @Bean
    fun processJob(step: Step): Job? {
        return jobFactory.get("HttpBatchJob")
            .incrementer(RunIdIncrementer())
            .start(step).build()
    }

    @Bean
    fun step(taskletReader: Tasklet): Step = stepBuilder
        .get("step")
        .tasklet(taskletReader)
        .build()

    @Bean
    @StepScope
    fun taskletReader(
        @Value("#{jobParameters['jobId']}") jobId: String,
        jobDetailRepository: JobDetailRepository,
        jobExecutionsRepository: JobExecutionsRepository,
        httpJobExecutor: HttpJobExecutor
    ): Tasklet {
        return JobDetailTasklet(
            jobId = jobId, jobDetailRepository = jobDetailRepository,
            jobExecutionsRepository = jobExecutionsRepository,
            httpJobExecutor = httpJobExecutor
        )
    }

//    @Bean
//    @StepScope
//    fun jobDetailReader(@Value("#{jobParameters['jobId']}") jobId: String,
//                        jobDetailRepository: JobDetailRepository,
//                        jobExecutionsRepository: JobExecutionsRepository): JobDetailReader {
//        return JobDetailReader(jobId = jobId, jobDetailRepository = jobDetailRepository)
//    }
//
//
//    @Bean
//    fun jobDetailWritter(): JobDetailWritter {
//        return JobDetailWritter()
//    }
//
//    @Bean
//    fun jobDetailProcessos(): JobDetailProcessor {
//        return JobDetailProcessor()
//    }

}