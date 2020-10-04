package br.com.diegosilva.sched.config

import br.com.diegosilva.sched.jobs.batch.JobDetailProcessor
import br.com.diegosilva.sched.jobs.batch.JobDetailReader
import br.com.diegosilva.sched.jobs.batch.JobDetailWritter
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing
@Configuration
class BatchConfig(val jobFactory: JobBuilderFactory, val stepBuilder: StepBuilderFactory) {

    @Bean
    fun processJob(): Job? {
        return jobFactory.get("processJob")
                .incrementer(RunIdIncrementer())
                .flow(step1()).end().build()
    }

    @Bean
    fun step1(): Step {
        return stepBuilder.get("orderStep1").chunk<String, String>(1)
                .reader(JobDetailReader())
                .processor(JobDetailProcessor()).writer(JobDetailWritter()).build()
    }
}