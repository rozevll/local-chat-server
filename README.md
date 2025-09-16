

<div align="center">

# 💬 Local Chat Server
**Spring Boot, WebFlux, Kotlin을 사용한 함수형 지역 기반 랜덤 채팅 서버**

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)
![WebFlux](https://img.shields.io/badge/WebFlux-Reactive-orange.svg)
![WebSocket](https://img.shields.io/badge/WebSocket-Real--time-purple.svg)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-green.svg)


[🚀 시작하기](#-시작하기) • [📚 API 문서](#-api-문서) • [🔧 기술 스택](#-기술-스택) • [📖 사용법](#-사용법)

</div>

---

## 주요 기능

- [x] 🔐 **사용자 인증**: 회원가입/로그인 시스템
- [ ] 👤 **지역별 사용자 배정**: 사용자의 지역 정보를 기반으로 매칭
- [x] 🎲 **랜덤 매칭**: 최대 4명까지 랜덤 채팅방 입장
- [x] 💬 **실시간 채팅**: WebSocket을 통한 즉시 메시지 전송
- [x] 🚪 **자유로운 퇴장**: 언제든지 채팅방 나가기 가능
- [x] 🏗️ **함수형 아키텍처**: Handler + RouterConfig 패턴으로 깔끔한 구조

## 🚀 시작하기

### 1️⃣ 사전 요구사항

- Java 17 이상
- Gradle 8.5 이상
- Redis (선택사항)

### 2️⃣ 설치 및 실행

```bash
# 저장소 클론
git clone <repository-url>
cd local-chat-server

# 애플리케이션 실행
./gradlew bootRun
```

### 3️⃣ 서버 접속

서버가 성공적으로 실행되면 다음 URL에서 접속할 수 있습니다:

- 🌐 **메인 서버**: http://localhost:7070
- 📖 **API 문서**: http://localhost:7070/swagger-ui/index.html

---

## 📚 API 문서

### 🔗 Swagger UI 접속

서버 실행 후 다음 URL에서 완전한 API 문서를 확인할 수 있습니다:

```
http://localhost:7070/swagger-ui/index.html
```

### 📋 API 엔드포인트 요약

| 카테고리 | 메서드 | 엔드포인트 | 설명 |
|---------|--------|-----------|------|
| **인증** | `POST` | `/api/auth/register` | 회원가입 |
| **인증** | `POST` | `/api/auth/login` | 로그인 |
| **채팅** | `GET` | `/api/chat/rooms` | 채팅방 목록 조회 |
| **채팅** | `POST` | `/api/chat/rooms/join` | 랜덤 채팅방 입장 |
| **채팅** | `POST` | `/api/chat/rooms/{roomId}/leave` | 채팅방 나가기 |
| **채팅** | `GET` | `/api/chat/rooms/{roomId}/messages` | 메시지 조회 |
| **채팅** | `POST` | `/api/chat/rooms/{roomId}/messages` | 메시지 전송 |
| **WebSocket** | `WS` | `/ws/chat?userId={userId}` | 실시간 채팅 연결 |

---

## 🔧 기술 스택

<div align="center">

| 카테고리 | 기술 | 버전 | 설명 |
|---------|------|------|------|
| **프레임워크** | Spring Boot | 3.2.0 | 메인 애플리케이션 프레임워크 |
| **언어** | Kotlin | 1.9.20 | 프로그래밍 언어 |
| **웹** | Spring WebFlux | - | 리액티브 웹 프레임워크 |
| **아키텍처** | 함수형 라우팅 | - | Handler + RouterConfig 패턴 |
| **통신** | WebSocket | - | 실시간 양방향 통신 |
| **문서화** | SpringDoc OpenAPI | 2.2.0 | API 문서 자동 생성 |
| **빌드** | Gradle | 8.5 | 빌드 도구 |
| **캐시** | Redis | - | 선택적 캐시 저장소 |

</div>

---

## 📖 사용법

### 🔐 1. 사용자 등록 및 로그인

```bash
# 회원가입
curl -X POST http://localhost:7070/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'

# 로그인
curl -X POST http://localhost:7070/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 🎲 2. 랜덤 채팅방 입장

```bash
curl -X POST "http://localhost:7070/api/chat/rooms/join?userId=USER_ID"
```

### 💬 3. 메시지 전송

```bash
curl -X POST "http://localhost:7070/api/chat/rooms/ROOM_ID/messages?userId=USER_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "안녕하세요! 👋"
  }'
```

### 🚪 4. 채팅방 나가기

```bash
curl -X POST "http://localhost:7070/api/chat/rooms/ROOM_ID/leave?userId=USER_ID"
```

---

## 🔌 WebSocket 연결

### 연결 URL
```
ws://localhost:7070/ws/chat?userId=YOUR_USER_ID
```

### 메시지 형식

#### 📤 클라이언트 → 서버
```json
{
  "type": "message",
  "content": "메시지 내용"
}
```

#### 📥 서버 → 클라이언트
```json
{
  "id": "message_id",
  "roomId": "room_id",
  "userId": "user_id",
  "username": "사용자명",
  "content": "메시지 내용",
  "timestamp": "2024-01-01T12:00:00",
  "messageType": "CHAT"
}
```

---