package br.com.diegosilva.sched.jobs.batch

import org.springframework.batch.item.ItemReader

class JobDetailReader: ItemReader<String> {

    private val messages = arrayOf("javainuse.com",
            "Welcome to Spring Batch Example",
            "We use H2 Database for this example")

    private var count = 0

    override fun read(): String? {
        if (count < messages.size) {
            return messages[count++];
        } else {
            count = 0;
        }
        return null;
    }
}