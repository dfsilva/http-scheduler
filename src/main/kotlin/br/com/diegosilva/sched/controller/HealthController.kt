package br.com.diegosilva.sched.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/health"], produces = ["text/plain;charset=utf-8"])
class HealthController {

    @GetMapping("alive")
    fun checkLiveness(): ResponseEntity<String> {
        return ResponseEntity.ok(mapOf("ok" to "true").toString())
    }

}