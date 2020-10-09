package br.com.diegosilva.sched.jobs.batch

import br.com.diegosilva.sched.model.HttpJobDetail
import org.springframework.batch.item.ItemProcessor

class JobDetailProcessor: ItemProcessor<HttpJobDetail, HttpJobDetail> {
    override fun process(item: HttpJobDetail): HttpJobDetail? {



        return item
    }
}