package br.com.diegosilva.sched.jobs.batch

import org.springframework.batch.item.ItemWriter


class JobDetailWritter: ItemWriter<String> {
    override fun write(messages: MutableList<out String>) {
        for (msg in messages) {
            println("Writing the data $msg")
        }
    }
}