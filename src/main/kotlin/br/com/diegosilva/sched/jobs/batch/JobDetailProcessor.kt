package br.com.diegosilva.sched.jobs.batch

import org.springframework.batch.item.ItemProcessor

class JobDetailProcessor: ItemProcessor<String, String> {
    override fun process(item: String): String? {
        return item.toUpperCase();
    }
}