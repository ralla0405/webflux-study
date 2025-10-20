# WebFlux LLM Chat Application

Spring WebFlux를 활용한 반응형(Reactive) LLM 호출 스터디 프로젝트입니다.
OpenAI GPT와 Google Gemini를 지원하며, 스트리밍 응답과 Chain of Thought 분석 기능을 제공합니다.

## 프로젝트 개요

이 프로젝트는 다음을 학습하고 실습하기 위한 스터디 프로젝트입니다:
- **Spring WebFlux**: 비동기/논블로킹 방식의 반응형 프로그래밍
- **LLM API 통합**: OpenAI GPT-4o, Google Gemini 2.0 Flash
- **실시간 스트리밍**: Server-Sent Events(SSE)를 통한 스트림 응답
- **Chain of Thought**: 단계별 문제 분석 및 해결

## 기술 스택

### Backend
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring WebFlux** (Project Reactor)
- **Gradle** (Kotlin DSL)
- **Lombok**

### Frontend
- **React 18.2.0**
- **Redux 5.0.1** + **Redux-Saga 1.3.0**
- **Axios 1.6.7**
- **Tailwind CSS 3.4.1**
- **Material-UI 5.15.14**
- **React-Markdown 9.0.1**

## 프로젝트 구조

```
webflux/
├── src/main/java/kr/io/wolverine/webflux/
│   ├── controller/          # REST API 엔드포인트
│   │   ├── facade/         # 프론트엔드용 Facade API
│   │   └── user/chat/      # 채팅 API
│   ├── service/            # 비즈니스 로직
│   │   ├── facade/
│   │   ├── llmclient/     # LLM 클라이언트 서비스
│   │   └── user/chat/     # 채팅 및 CoT 서비스
│   ├── model/             # DTO 및 데이터 모델
│   │   ├── facade/
│   │   ├── llmclient/     # LLM 요청/응답 모델
│   │   │   ├── gpt/
│   │   │   ├── gemini/
│   │   │   └── jsonformat/
│   │   └── user/chat/
│   ├── exception/         # 전역 예외 처리
│   ├── config/            # 설정 클래스
│   └── util/              # 유틸리티
├── webflux-front/         # React 프론트엔드
│   ├── src/
│   │   ├── components/    # React 컴포넌트
│   │   ├── reducer/       # Redux 리듀서
│   │   ├── saga/          # Redux-Saga
│   │   ├── store/         # Redux 스토어
│   │   └── utils/
│   └── public/
└── build.gradle.kts       # Gradle 빌드 설정
```

## 주요 기능

### 1. Multi-LLM 지원
- **OpenAI GPT-4o**: 고성능 대화형 AI
- **Google Gemini 2.0 Flash**: 빠른 응답 속도의 AI 모델
- 런타임 시 LLM 제공자 동적 선택 가능

### 2. 반응형 스트리밍
- `Flux<UserChatResponseDto>`를 사용한 실시간 스트리밍 응답
- Server-Sent Events(SSE)로 프론트엔드에 전달
- 비동기/논블로킹 방식으로 처리

### 3. Chain of Thought (CoT)
3단계로 구성된 문제 분석 프로세스:
1. **문제 분석**: 필요한 분석 단계 도출
2. **순차 분석**: 각 단계별 상세 분석 수행
3. **최종 답변**: 모든 분석 결과를 종합하여 답변 생성

### 4. API 엔드포인트

| Endpoint | Method | 설명 | 응답 타입 |
|----------|--------|------|-----------|
| `/facade/home` | POST | 사용 가능한 LLM 모델 목록 조회 | `Mono<FacadeHomeResponseDto>` |
| `/chat/oneshot` | POST | 단일 질의/응답 | `Mono<UserChatResponseDto>` |
| `/chat/oneshot/stream` | POST | 스트리밍 응답 | `Flux<UserChatResponseDto>` |
| `/chat/cot` | POST | Chain of Thought 분석 | `Flux<UserChatResponseDto>` |

## 설치 및 실행

### 사전 요구사항
- Java 21
- Node.js 16+
- OpenAI API Key
- Google Gemini API Key

### Backend 실행

1. API 키 설정
```yaml
# src/main/resources/application.yml
llm:
  gpt:
    key: "YOUR_OPENAI_API_KEY"
  gemini:
    key: "YOUR_GEMINI_API_KEY"
```

2. 애플리케이션 실행
```bash
./gradlew bootRun
```

백엔드는 기본적으로 `http://localhost:8080`에서 실행됩니다.

### Frontend 실행

1. 의존성 설치
```bash
cd webflux-front
npm install
```

2. 개발 서버 실행
```bash
npm start
```

프론트엔드는 `http://localhost:3334`에서 실행됩니다.

3. 프로덕션 빌드
```bash
npm run build
```

## WebFlux 학습 포인트

### 1. 반응형 프로그래밍 (Reactive Programming)
```java
// Mono: 0 또는 1개의 결과 반환
public Mono<UserChatResponseDto> sendOneShotRequest(UserChatRequestDto requestDto)

// Flux: 0개 이상의 결과 스트림 반환
public Flux<UserChatResponseDto> sendStreamRequest(UserChatRequestDto requestDto)
```

### 2. WebClient를 통한 비동기 HTTP 호출
```java
webClient.post()
    .uri(apiUrl)
    .header("Authorization", "Bearer " + apiKey)
    .bodyValue(requestDto)
    .retrieve()
    .bodyToFlux(ResponseDto.class)
```

### 3. 에러 핸들링
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ErrorTypeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleErrorTypeException(
        ErrorTypeException e) {
        // 반응형 방식의 예외 처리
    }
}
```

### 4. CORS 설정
```java
@Configuration
public class CorsGlobalConfig implements WebFluxConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*");
    }
}
```

## LLM 통합 아키텍처

### Strategy Pattern을 통한 LLM 제공자 선택
```java
@Configuration
public class CommonConfig {
    @Bean
    public Map<LlmType, LlmWebClientService> llmServiceMap(
        GptWebClientService gptService,
        GeminiWebClientService geminiService) {
        return Map.of(
            LlmType.GPT, gptService,
            LlmType.GEMINI, geminiService
        );
    }
}
```

이를 통해 런타임에 동적으로 LLM 제공자를 선택할 수 있습니다.

## Docker 배포

### Frontend Docker 빌드
```bash
cd webflux-front
docker build -t webflux-front .
docker run -p 80:80 webflux-front
```

### GCP Cloud Build 지원
`webflux-front/cloudbuild.yaml` 파일을 통해 Google Cloud Platform에 자동 배포 가능

## 프로젝트 목표

- [x] Spring WebFlux 기본 설정
- [x] OpenAI GPT API 통합
- [x] Google Gemini API 통합
- [x] 스트리밍 응답 구현
- [x] Chain of Thought 구현
- [x] React 프론트엔드 구현
- [x] Redux를 통한 상태 관리
- [x] SSE 기반 실시간 스트리밍

## 학습 자료

- [Spring WebFlux 공식 문서](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Project Reactor](https://projectreactor.io/)
- [OpenAI API 문서](https://platform.openai.com/docs/api-reference)
- [Google Gemini API 문서](https://ai.google.dev/docs)

## 라이선스

이 프로젝트는 학습 목적의 스터디 프로젝트입니다.

## 기여

스터디 프로젝트이므로 자유롭게 fork하여 학습하실 수 있습니다.