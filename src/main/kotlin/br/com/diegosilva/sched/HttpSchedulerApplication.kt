package br.com.diegosilva.sched

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class HttpSchedulerApplication

fun main(args: Array<String>) {
    runApplication<HttpSchedulerApplication>(*args)
}
