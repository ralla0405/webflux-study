package kr.io.wolverine.webflux.service.llmclient;

import kr.io.wolverine.webflux.exception.CommonError;
import kr.io.wolverine.webflux.exception.ErrorTypeException;
import kr.io.wolverine.webflux.model.llmclient.LlmChatRequestDto;
import kr.io.wolverine.webflux.model.llmclient.LlmChatResponseDto;
import kr.io.wolverine.webflux.model.llmclient.LlmType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LlmWebClientService {

    Mono<LlmChatResponseDto> getChatCompletion(LlmChatRequestDto llmChatRequestDto);

    default Mono<LlmChatResponseDto> getChatCompletionWithCatchException(LlmChatRequestDto requestDto) {
        return getChatCompletion(requestDto)
                .onErrorResume(exception -> {
                    if (exception instanceof ErrorTypeException errorTypeException) {
                        CommonError commonError = new CommonError(errorTypeException.getErrorType().getCode(), errorTypeException.getMessage());
                        return Mono.just(new LlmChatResponseDto(commonError, errorTypeException));
                    } else {
                        CommonError commonError = new CommonError(500, exception.getMessage());
                        return Mono.just(new LlmChatResponseDto(commonError, exception));
                    }
                });
    }

    LlmType getLlmType();

    Flux<LlmChatResponseDto> getChatCompletionStream(LlmChatRequestDto llmChatRequestDto);
}
