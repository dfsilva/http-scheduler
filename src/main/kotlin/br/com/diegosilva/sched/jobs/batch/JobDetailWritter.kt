package br.com.diegosilva.sched.jobs.batch

import br.com.diegosilva.sched.model.HttpJobDetail
import org.springframework.batch.item.ItemWriter


class JobDetailWritter : ItemWriter<HttpJobDetail> {
    override fun write(messages: MutableList<out HttpJobDetail>) {
        for (msg in messages) {
            println("Processou jobdeatail ${msg.jobId}")
        }
    }
}