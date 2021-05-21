package br.com.diegosilva.sched.jobs.scheduler

import br.com.diegosilva.sched.service.SchedulerService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProcessFailedBatchsScheduler(val schedulerService: SchedulerService) {

    private val log = LoggerFactory.getLogger(ProcessFailedBatchsScheduler::class.java)

    @Scheduled(cron = "0 0 0/1 * * *")//executar a cada hora
    fun executar() {
        log.debug("----Reprocessando batchs que falharam")
        schedulerService.runFailedJobs()
    }
}