package com.example.localchatserver.config

import com.example.localchatserver.websocket.ChatWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketConfig {
    
    @Bean
    fun handlerMapping(chatWebSocketHandler: ChatWebSocketHandler): HandlerMapping {
        val map = mapOf("/ws/chat" to chatWebSocketHandler)
        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = -1
        return mapping
    }
    
    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }
}

