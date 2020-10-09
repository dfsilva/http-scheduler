package br.com.diegosilva.sched.config

import br.com.diegosilva.sched.executors.HttpJobExecutor
import br.com.diegosilva.sched.jobs.batch.JobDetailProcessor
import br.com.diegosilva.sched.jobs.batch.JobDetailReader
import br.com.diegosilva.sched.jobs.batch.JobDetailTasklet
import br.com.diegosilva.sched.jobs.batch.JobDetailWritter
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

@EnableBatchProcessing
@Configuration
class BatchConfig(val jobFactory: JobBuilderFactory, val stepBuilder: StepBuilderFactory) {

    @Bean
    fun processJob(step: Step): Job? {
        return jobFactory.get("httpBatchJob")
                .incrementer(RunIdIncrementer())
                .start(step).build()
    }

    @Bean
    fun step(jobDetailReader: JobDetailReader,
             jobDetailProcessos: JobDetailProcessor,
             jobDetailWritter: JobDetailWritter, taskletReader: Tasklet): Step =
            stepBuilder.get("httpBatchStep")
//                    .chunk<HttpJobDetail, HttpJobDetail>(1)
                    .tasklet(taskletReader)
//                    .reader(jobDetailReader)
//                    .processor(jobDetailProcessos)
//                    .writer(jobDetailWritter)
                    .build()


    @Bean
    @StepScope
    fun taskletReader(@Value("#{jobParameters['jobId']}") jobId: String,
                      jobDetailRepository: JobDetailRepository,
                      jobExecutionsRepository: JobExecutionsRepository,
                      httpJobExecutor: HttpJobExecutor): Tasklet {
        return JobDetailTasklet(
                jobId = jobId, jobDetailRepository = jobDetailRepository,
                jobExecutionsRepository = jobExecutionsRepository,
                httpJobExecutor = httpJobExecutor
        )
    }

    @Bean
    @StepScope
    fun jobDetailReader(@Value("#{jobParameters['jobId']}") jobId: String,
                        jobDetailRepository: JobDetailRepository,
                        jobExecutionsRepository: JobExecutionsRepository): JobDetailReader {
        return JobDetailReader(jobId = jobId, jobDetailRepository = jobDetailRepository)
    }


    @Bean
    fun jobDetailWritter(): JobDetailWritter {
        return JobDetailWritter()
    }

    @Bean
    fun jobDetailProcessos(): JobDetailProcessor {
        return JobDetailProcessor()
    }

}