package br.com.diegosilva.sched.jobs.scheduler

import br.com.diegosilva.sched.service.SchedulerService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProcessFailedBatchsScheduler(val schedulerService: SchedulerService) {

    private val log = LoggerFactory.getLogger(ProcessFailedBatchsScheduler::class.java)

    //    @Scheduled(cron = "0 15 0 * * ?")
    @Scheduled(cron = "0 0/1 * * * *")
    fun executar() {
        log.debug("----Reprocessando batchs que falharam")

    }

//    @Scheduled(cron = "1 0 0 0 0 *")
//    fun executarAoIniciar() {
//        log.debug("--Reprocessando batchs que falharam")
//    }
}