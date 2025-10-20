package kr.io.wolverine.webflux.service.user.chat;

import kr.io.wolverine.webflux.model.llmclient.LlmChatRequestDto;
import kr.io.wolverine.webflux.model.llmclient.LlmChatResponseDto;
import kr.io.wolverine.webflux.model.llmclient.LlmType;
import kr.io.wolverine.webflux.model.user.chat.UserChatRequestDto;
import kr.io.wolverine.webflux.model.user.chat.UserChatResponseDto;
import kr.io.wolverine.webflux.service.llmclient.LlmWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserChatServiceImpl implements UserChatService {

    private final Map<LlmType, LlmWebClientService> llmWebClientServiceMap;

    @Override
    public Mono<UserChatResponseDto> getOneShotChat(UserChatRequestDto userChatRequestDto) {
        LlmChatRequestDto llmChatRequestDto = new LlmChatRequestDto(userChatRequestDto, "요청에 적절히 응답해주세요.");

        Mono<LlmChatResponseDto> chatCompletionMono = llmWebClientServiceMap.get(userChatRequestDto.llmModel().getLlmType())
                .getChatCompletion(llmChatRequestDto);

        return chatCompletionMono.map(UserChatResponseDto::new);
    }
}
