package com.example.localchatserver.websocket

import com.example.localchatserver.model.ChatMessageRequest
import com.example.localchatserver.service.ChatRoomService
import com.example.localchatserver.service.ChatService
import com.example.localchatserver.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatWebSocketHandler(
    private val chatService: ChatService,
    private val chatRoomService: ChatRoomService,
    private val userService: UserService,
    private val objectMapper: ObjectMapper
) : WebSocketHandler {
    
    private val userSessions = ConcurrentHashMap<String, WebSocketSession>()
    private val userRooms = ConcurrentHashMap<String, String>() // userId -> roomId
    
    override fun handle(session: WebSocketSession): Mono<Void> {
        val userId = extractUserIdFromSession(session)
        
        return session.receive()
            .map { message ->
                val payload = message.payloadAsText
                val messageData = objectMapper.readValue(payload, Map::class.java)
                handleMessage(userId, messageData, session)
            }
            .then()
            .doOnSubscribe {
                userSessions[userId] = session
                joinRoom(userId)
            }
            .doFinally {
                userSessions.remove(userId)
                leaveRoom(userId)
            }
    }
    
    private fun extractUserIdFromSession(session: WebSocketSession): String {
        // 실제로는 세션에서 사용자 ID를 추출해야 함
        // 여기서는 간단히 쿼리 파라미터에서 추출
        val query = session.handshakeInfo.uri.query
        return query.split("&")
            .find { it.startsWith("userId=") }
            ?.substringAfter("userId=")
            ?: throw IllegalArgumentException("User ID not provided")
    }
    
    private fun handleMessage(userId: String, messageData: Map<*, *>, @Suppress("UNUSED_PARAMETER") session: WebSocketSession): Mono<Void> {
        return when (messageData["type"]) {
            "join" -> joinRoom(userId)
            "leave" -> leaveRoom(userId)
            "message" -> {
                val content = messageData["content"] as? String
                if (content != null) {
                    sendMessage(userId, ChatMessageRequest(content))
                } else {
                    Mono.empty()
                }
            }
            else -> Mono.empty()
        }.then()
    }
    
    private fun joinRoom(userId: String): Mono<Void> {
        return chatRoomService.joinRoom(userId)
            .flatMap { room ->
                userRooms[userId] = room.id
                userService.findById(userId)
                    .flatMap { user ->
                        chatService.notifyUserJoin(room.id, user.username)
                    }
            }
            .then()
    }
    
    private fun leaveRoom(userId: String): Mono<Void> {
        val roomId = userRooms[userId]
        return if (roomId != null) {
            userService.findById(userId)
                .flatMap { user ->
                    chatService.notifyUserLeave(roomId, user.username)
                        .then(chatRoomService.leaveRoom(userId, roomId))
                }
                .doFinally {
                    userRooms.remove(userId)
                }
                .then()
        } else {
            Mono.empty()
        }
    }
    
    private fun sendMessage(userId: String, request: ChatMessageRequest): Mono<Void> {
        val roomId = userRooms[userId]
        return if (roomId != null) {
            userService.findById(userId)
                .flatMap { user ->
                    chatService.sendMessage(roomId, userId, user.username, request)
                }
                .then()
        } else {
            Mono.empty()
        }
    }
}
