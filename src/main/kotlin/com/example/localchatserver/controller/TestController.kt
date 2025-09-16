package com.example.localchatserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/test")
class TestController {
    
    @GetMapping("/hello")
    fun hello(): Mono<String> {
        return Mono.just("Hello from WebFlux!")
    }
}
