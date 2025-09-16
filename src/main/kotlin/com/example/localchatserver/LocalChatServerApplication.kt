package com.example.localchatserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LocalChatServerApplication

fun main(args: Array<String>) {
    runApplication<LocalChatServerApplication>(*args)
}

