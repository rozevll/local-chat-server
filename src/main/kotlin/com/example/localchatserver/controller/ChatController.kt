package com.example.localchatserver.controller

import com.example.localchatserver.model.ChatMessageRequest
import com.example.localchatserver.model.ChatMessageResponse
import com.example.localchatserver.model.ChatRoomResponse
import com.example.localchatserver.service.ChatRoomService
import com.example.localchatserver.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chat")
@Tag(name = "채팅", description = "채팅방 및 메시지 관련 API")
class ChatController(
    private val chatRoomService: ChatRoomService,
    private val chatService: ChatService
) {
    
    @GetMapping("/rooms")
    @Operation(summary = "채팅방 목록 조회", description = "활성화된 모든 채팅방 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공")
        ]
    )
    fun getAllRooms(): Mono<ResponseEntity<List<ChatRoomResponse>>> {
        return chatRoomService.getAllActiveRooms()
            .map { ResponseEntity.ok(it) }
    }
    
    @GetMapping("/rooms/{roomId}/messages")
    @Operation(summary = "채팅방 메시지 조회", description = "특정 채팅방의 메시지 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공"),
            ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
        ]
    )
    fun getMessages(
        @Parameter(description = "채팅방 ID") @PathVariable roomId: String
    ): Mono<ResponseEntity<List<ChatMessageResponse>>> {
        return chatService.getMessages(roomId)
            .map { messages ->
                val responses = messages.map { message ->
                    ChatMessageResponse(
                        id = message.id,
                        roomId = message.roomId,
                        userId = message.userId,
                        username = message.username,
                        content = message.content,
                        timestamp = message.timestamp,
                        messageType = message.messageType
                    )
                }
                ResponseEntity.ok(responses)
            }
    }
    
    @PostMapping("/rooms/{roomId}/messages")
    @Operation(summary = "메시지 전송", description = "채팅방에 메시지를 전송합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "메시지 전송 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 (방에 참여하지 않은 사용자 등)")
        ]
    )
    fun sendMessage(
        @Parameter(description = "채팅방 ID") @PathVariable roomId: String,
        @Parameter(description = "사용자 ID") @RequestParam userId: String,
        @RequestBody request: ChatMessageRequest
    ): Mono<ResponseEntity<ChatMessageResponse>> {
        return chatRoomService.getRoom(roomId)
            .flatMap { room ->
                if (room.participants.contains(userId)) {
                    chatService.sendMessage(roomId, userId, "User$userId", request)
                        .map { message ->
                            ResponseEntity.ok(
                                ChatMessageResponse(
                                    id = message.id,
                                    roomId = message.roomId,
                                    userId = message.userId,
                                    username = message.username,
                                    content = message.content,
                                    timestamp = message.timestamp,
                                    messageType = message.messageType
                                )
                            )
                        }
                } else {
                    Mono.just(ResponseEntity.badRequest().build())
                }
            }
    }
    
    @PostMapping("/rooms/join")
    @Operation(summary = "랜덤 채팅방 입장", description = "사용자를 랜덤 채팅방에 입장시킵니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "채팅방 입장 성공"),
            ApiResponse(responseCode = "400", description = "입장 실패")
        ]
    )
    fun joinRoom(
        @Parameter(description = "사용자 ID") @RequestParam userId: String
    ): Mono<ResponseEntity<ChatRoomResponse>> {
        return chatRoomService.joinRoom(userId)
            .map { room ->
                ResponseEntity.ok(
                    ChatRoomResponse(
                        id = room.id,
                        name = room.name,
                        participantCount = room.participants.size,
                        maxParticipants = room.maxParticipants,
                        createdAt = room.createdAt
                    )
                )
            }
    }
    
    @PostMapping("/rooms/{roomId}/leave")
    @Operation(summary = "채팅방 나가기", description = "사용자가 채팅방에서 나갑니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "채팅방 나가기 성공"),
            ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
        ]
    )
    fun leaveRoom(
        @Parameter(description = "채팅방 ID") @PathVariable roomId: String,
        @Parameter(description = "사용자 ID") @RequestParam userId: String
    ): Mono<ResponseEntity<String>> {
        return chatRoomService.leaveRoom(userId, roomId)
            .map { ResponseEntity.ok("Left room successfully") }
    }
}
