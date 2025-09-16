package com.example.localchatserver.service

import com.example.localchatserver.model.ChatMessage
import com.example.localchatserver.model.ChatMessageRequest
import com.example.localchatserver.model.MessageType
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatService {
    
    private val messages = ConcurrentHashMap<String, MutableList<ChatMessage>>()
    private val messageFluxes = ConcurrentHashMap<String, reactor.core.publisher.FluxSink<ChatMessage>>()
    
    fun sendMessage(roomId: String, userId: String, username: String, request: ChatMessageRequest): Mono<ChatMessage> {
        return Mono.fromCallable {
            val message = ChatMessage(
                id = UUID.randomUUID().toString(),
                roomId = roomId,
                userId = userId,
                username = username,
                content = request.content,
                timestamp = LocalDateTime.now(),
                messageType = MessageType.CHAT
            )
            
            // 메시지 저장
            messages.computeIfAbsent(roomId) { mutableListOf() }.add(message)
            
            // 실시간 전송
            messageFluxes[roomId]?.next(message)
            
            message
        }
    }
    
    fun sendSystemMessage(roomId: String, content: String): Mono<ChatMessage> {
        return Mono.fromCallable {
            val message = ChatMessage(
                id = UUID.randomUUID().toString(),
                roomId = roomId,
                userId = "system",
                username = "System",
                content = content,
                timestamp = LocalDateTime.now(),
                messageType = MessageType.SYSTEM
            )
            
            messages.computeIfAbsent(roomId) { mutableListOf() }.add(message)
            messageFluxes[roomId]?.next(message)
            
            message
        }
    }
    
    fun getMessages(roomId: String): Mono<List<ChatMessage>> {
        return Mono.fromCallable {
            messages[roomId]?.toList() ?: emptyList()
        }
    }
    
    fun getMessageStream(roomId: String): Flux<ChatMessage> {
        return Flux.create { sink ->
            messageFluxes[roomId] = sink
            
            sink.onDispose {
                messageFluxes.remove(roomId)
            }
        }
    }
    
    fun notifyUserJoin(roomId: String, username: String): Mono<ChatMessage> {
        return sendSystemMessage(roomId, "${username}님이 입장했습니다.")
    }
    
    fun notifyUserLeave(roomId: String, username: String): Mono<ChatMessage> {
        return sendSystemMessage(roomId, "${username}님이 퇴장했습니다.")
    }
}
