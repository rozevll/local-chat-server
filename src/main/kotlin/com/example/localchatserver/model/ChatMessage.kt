package com.example.localchatserver.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채팅 메시지")
data class ChatMessage(
    @Schema(description = "메시지 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: String,
    
    @Schema(description = "채팅방 ID", example = "room-123")
    val roomId: String,
    
    @Schema(description = "사용자 ID", example = "user-456")
    val userId: String,
    
    @Schema(description = "사용자명", example = "john_doe")
    val username: String,
    
    @Schema(description = "메시지 내용", example = "안녕하세요!")
    val content: String,
    
    @Schema(description = "전송 시간", example = "2024-01-01T12:00:00")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @Schema(description = "메시지 타입", example = "CHAT")
    val messageType: MessageType = MessageType.CHAT
)

@Schema(description = "메시지 타입")
enum class MessageType {
    @Schema(description = "일반 채팅 메시지")
    CHAT,
    
    @Schema(description = "사용자 입장 메시지")
    JOIN,
    
    @Schema(description = "사용자 퇴장 메시지")
    LEAVE,
    
    @Schema(description = "시스템 메시지")
    SYSTEM
}

@Schema(description = "메시지 전송 요청")
data class ChatMessageRequest(
    @Schema(description = "메시지 내용", example = "안녕하세요!", required = true)
    val content: String
)

@Schema(description = "메시지 응답")
data class ChatMessageResponse(
    @Schema(description = "메시지 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: String,
    
    @Schema(description = "채팅방 ID", example = "room-123")
    val roomId: String,
    
    @Schema(description = "사용자 ID", example = "user-456")
    val userId: String,
    
    @Schema(description = "사용자명", example = "john_doe")
    val username: String,
    
    @Schema(description = "메시지 내용", example = "안녕하세요!")
    val content: String,
    
    @Schema(description = "전송 시간", example = "2024-01-01T12:00:00")
    val timestamp: LocalDateTime,
    
    @Schema(description = "메시지 타입", example = "CHAT")
    val messageType: MessageType
)
