package com.example.localchatserver.controller

import com.example.localchatserver.model.UserLoginRequest
import com.example.localchatserver.model.UserRegisterRequest
import com.example.localchatserver.model.UserResponse
import com.example.localchatserver.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증", description = "사용자 인증 관련 API")
class AuthController(
    private val userService: UserService
) {
    
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "회원가입 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 (중복 사용자명 등)")
        ]
    )
    fun register(@RequestBody request: UserRegisterRequest): Mono<ResponseEntity<UserResponse>> {
        return userService.register(request)
            .map { user ->
                ResponseEntity.ok(
                    UserResponse(
                        id = user.id,
                        username = user.username,
                        createdAt = user.createdAt
                    )
                )
            }
            .onErrorReturn(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            )
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증을 수행합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패")
        ]
    )
    fun login(@RequestBody request: UserLoginRequest): Mono<ResponseEntity<UserResponse>> {
        return userService.login(request)
            .map { user ->
                ResponseEntity.ok(
                    UserResponse(
                        id = user.id,
                        username = user.username,
                        createdAt = user.createdAt
                    )
                )
            }
            .onErrorReturn(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            )
    }
}
