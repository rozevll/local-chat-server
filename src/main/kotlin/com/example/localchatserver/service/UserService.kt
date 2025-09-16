package com.example.localchatserver.service

import com.example.localchatserver.model.User
import com.example.localchatserver.model.UserLoginRequest
import com.example.localchatserver.model.UserRegisterRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class UserService {
    
    private val users = ConcurrentHashMap<String, User>()
    
    fun register(request: UserRegisterRequest): Mono<User> {
        return Mono.fromCallable {
            // 중복 사용자명 체크
            if (users.values.any { it.username == request.username }) {
                throw IllegalArgumentException("Username already exists")
            }
            
            val user = User(
                id = UUID.randomUUID().toString(),
                username = request.username,
                password = request.password // 실제로는 암호화해야 함
            )
            
            users[user.id] = user
            user
        }
    }
    
    fun login(request: UserLoginRequest): Mono<User> {
        return Mono.fromCallable {
            users.values.find { it.username == request.username && it.password == request.password }
                ?: throw IllegalArgumentException("Invalid username or password")
        }
    }
    
    fun findById(userId: String): Mono<User> {
        return Mono.fromCallable {
            users[userId] ?: throw IllegalArgumentException("User not found")
        }
    }
    
    fun findByUsername(username: String): Mono<User> {
        return Mono.fromCallable {
            users.values.find { it.username == username }
                ?: throw IllegalArgumentException("User not found")
        }
    }
}

