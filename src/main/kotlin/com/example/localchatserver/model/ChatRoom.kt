package com.example.localchatserver.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채팅방")
data class ChatRoom(
    @Schema(description = "채팅방 ID", example = "room-123")
    val id: String,
    
    @Schema(description = "채팅방 이름", example = "Room 1")
    val name: String,
    
    @Schema(description = "참여자 ID 목록")
    val participants: MutableSet<String> = mutableSetOf(), // User IDs
    
    @Schema(description = "최대 참여자 수", example = "4")
    val maxParticipants: Int = 4,
    
    @Schema(description = "생성일시", example = "2024-01-01T12:00:00")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Schema(description = "활성화 상태", example = "true")
    val isActive: Boolean = true
) {
    fun addParticipant(userId: String): Boolean {
        return if (participants.size < maxParticipants && !participants.contains(userId)) {
            participants.add(userId)
            true
        } else {
            false
        }
    }
    
    fun removeParticipant(userId: String): Boolean {
        return participants.remove(userId)
    }
    
    fun isFull(): Boolean = participants.size >= maxParticipants
    
    fun isEmpty(): Boolean = participants.isEmpty()
}

@Schema(description = "채팅방 응답")
data class ChatRoomResponse(
    @Schema(description = "채팅방 ID", example = "room-123")
    val id: String,
    
    @Schema(description = "채팅방 이름", example = "Room 1")
    val name: String,
    
    @Schema(description = "현재 참여자 수", example = "2")
    val participantCount: Int,
    
    @Schema(description = "최대 참여자 수", example = "4")
    val maxParticipants: Int,
    
    @Schema(description = "생성일시", example = "2024-01-01T12:00:00")
    val createdAt: LocalDateTime
)
