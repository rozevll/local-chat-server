package com.example.localchatserver.service

import com.example.localchatserver.model.ChatRoom
import com.example.localchatserver.model.ChatRoomResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatRoomService {
    
    private val chatRooms = ConcurrentHashMap<String, ChatRoom>()
    
    fun findAvailableRoom(): Mono<ChatRoom> {
        return Mono.fromCallable {
            // 빈 방이 있으면 반환
            val emptyRoom = chatRooms.values.find { it.isEmpty() }
            if (emptyRoom != null) {
                emptyRoom
            } else {
                // 빈 방이 없으면 새로 생성
                createNewRoom()
            }
        }
    }
    
    fun joinRoom(userId: String): Mono<ChatRoom> {
        return findAvailableRoom()
            .flatMap { room ->
                Mono.fromCallable {
                    if (room.addParticipant(userId)) {
                        room
                    } else {
                        throw IllegalStateException("Cannot join room")
                    }
                }
            }
    }
    
    fun leaveRoom(userId: String, roomId: String): Mono<ChatRoom> {
        return Mono.fromCallable {
            val room = chatRooms[roomId] ?: throw IllegalArgumentException("Room not found")
            room.removeParticipant(userId)
            
            // 방이 비어있으면 비활성화
            if (room.isEmpty()) {
                // isActive는 val이므로 수정할 수 없음. 대신 새로운 방 객체를 생성하거나 다른 방식으로 처리
                // 여기서는 단순히 빈 방을 유지
            }
            
            room
        }
    }
    
    fun getRoom(roomId: String): Mono<ChatRoom> {
        return Mono.fromCallable {
            chatRooms[roomId] ?: throw IllegalArgumentException("Room not found")
        }
    }
    
    fun getRoomParticipants(roomId: String): Mono<Set<String>> {
        return getRoom(roomId)
            .map { it.participants.toSet() }
    }
    
    private fun createNewRoom(): ChatRoom {
        val room = ChatRoom(
            id = UUID.randomUUID().toString(),
            name = "Room ${chatRooms.size + 1}"
        )
        chatRooms[room.id] = room
        return room
    }
    
    fun getAllActiveRooms(): Mono<List<ChatRoomResponse>> {
        return Mono.fromCallable {
            chatRooms.values
                .filter { it.isActive }
                .map { room ->
                    ChatRoomResponse(
                        id = room.id,
                        name = room.name,
                        participantCount = room.participants.size,
                        maxParticipants = room.maxParticipants,
                        createdAt = room.createdAt
                    )
                }
                .toList()
        }
    }
}
