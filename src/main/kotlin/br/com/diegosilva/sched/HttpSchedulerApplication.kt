package br.com.diegosilva.sched

//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableRetry
@SpringBootApplication
class HttpSchedulerApplication

fun main(args: Array<String>) {
    runApplication<HttpSchedulerApplication>(*args)
}
