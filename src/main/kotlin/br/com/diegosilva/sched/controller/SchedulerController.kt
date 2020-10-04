package br.com.diegosilva.sched.controller

import br.com.diegosilva.sched.model.HttpJobDetail
import br.com.diegosilva.sched.service.SchedulerService
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletionStage

@RestController
@RequestMapping(value = ["/sched-api"])
class SchedulerController(val schedService: SchedulerService) {

    @PostMapping
    fun create(@RequestBody jobDetail: HttpJobDetail): CompletionStage<HttpJobDetail> {
        return schedService.scheduler(jobDetail)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") jobId: String): CompletionStage<Boolean> {
        return schedService.delete(jobId)
    }

    @PutMapping
    fun update(@RequestBody jobDetail: HttpJobDetail): CompletionStage<HttpJobDetail> {
        return schedService.scheduler(jobDetail)
    }
}