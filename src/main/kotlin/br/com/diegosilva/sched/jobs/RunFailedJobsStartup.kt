package br.com.diegosilva.sched.jobs

import br.com.diegosilva.sched.repository.HttpRunningJobRepository
import br.com.diegosilva.sched.service.SchedulerService
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class RunFailedJobsStartup(val runningJobRepository: HttpRunningJobRepository, val schedulerService: SchedulerService) :
    InitializingBean {
    override fun afterPropertiesSet() {
        runningJobRepository.deleteAll()
        schedulerService.runFailedJobs();
    }
}