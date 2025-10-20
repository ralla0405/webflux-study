package kr.io.wolverine.webflux.service.user.chat;

import kr.io.wolverine.webflux.model.user.chat.UserChatRequestDto;
import kr.io.wolverine.webflux.model.user.chat.UserChatResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserChatService {

    Mono<UserChatResponseDto> getOneShotChat(UserChatRequestDto userChatRequestDto);

    Flux<UserChatResponseDto> getOneShotChatStream(UserChatRequestDto userChatRequestDto);
}
