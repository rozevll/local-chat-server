package com.example.localchatserver.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "사용자 정보")
data class User(
    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: String,
    
    @Schema(description = "사용자명", example = "john_doe")
    val username: String,
    
    @Schema(description = "비밀번호", example = "password123")
    val password: String,
    
    @Schema(description = "생성일시", example = "2024-01-01T12:00:00")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Schema(description = "로그인 요청")
data class UserLoginRequest(
    @Schema(description = "사용자명", example = "john_doe", required = true)
    val username: String,
    
    @Schema(description = "비밀번호", example = "password123", required = true)
    val password: String
)

@Schema(description = "회원가입 요청")
data class UserRegisterRequest(
    @Schema(description = "사용자명", example = "john_doe", required = true)
    val username: String,
    
    @Schema(description = "비밀번호", example = "password123", required = true)
    val password: String
)

@Schema(description = "사용자 응답")
data class UserResponse(
    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: String,
    
    @Schema(description = "사용자명", example = "john_doe")
    val username: String,
    
    @Schema(description = "생성일시", example = "2024-01-01T12:00:00")
    val createdAt: LocalDateTime
)
