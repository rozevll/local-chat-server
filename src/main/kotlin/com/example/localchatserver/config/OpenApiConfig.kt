package com.example.localchatserver.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Local Chat Server API")
                    .description("Spring Boot, WebFlux, Kotlin을 사용한 랜덤 채팅 서버 API")
                    .version("v1.0.0")
            )
            .addServersItem(
                Server()
                    .url("http://localhost:7070")
                    .description("로컬 개발 서버")
            )
    }
    
    @Bean
    fun api(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("api")
            .pathsToMatch("/api/**")
            .build()
    }
}

