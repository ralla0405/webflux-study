package kr.io.wolverine.webflux.service.llmclient;

import kr.io.wolverine.webflux.model.llmclient.LlmChatRequestDto;
import kr.io.wolverine.webflux.model.llmclient.LlmChatResponseDto;
import kr.io.wolverine.webflux.model.llmclient.LlmType;
import reactor.core.publisher.Mono;

public interface LlmWebClientService {

    Mono<LlmChatResponseDto> getChatCompletion(LlmChatRequestDto llmChatRequestDto);
    LlmType getLlmType();
}
