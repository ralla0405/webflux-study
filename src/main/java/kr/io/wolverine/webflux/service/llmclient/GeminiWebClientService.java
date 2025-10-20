package kr.io.wolverine.webflux.service.llmclient;

import kr.io.wolverine.webflux.model.llmclient.LlmChatRequestDto;
import kr.io.wolverine.webflux.model.llmclient.LlmChatResponseDto;
import kr.io.wolverine.webflux.model.llmclient.LlmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiWebClientService implements LlmWebClientService {

    @Override
    public Mono<LlmChatResponseDto> getChatCompletion(LlmChatRequestDto llmChatRequestDto) {
        return null;
    }

    @Override
    public LlmType getLlmType() {
        return LlmType.GEMINI;
    }
}
