package kr.io.wolverine.webflux.controller.user.chat;

import kr.io.wolverine.webflux.model.user.chat.UserChatRequestDto;
import kr.io.wolverine.webflux.model.user.chat.UserChatResponseDto;
import kr.io.wolverine.webflux.service.user.chat.ChainOfThoughtService;
import kr.io.wolverine.webflux.service.user.chat.UserChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class UserChatController {

    private final UserChatService userChatService;
    private final ChainOfThoughtService chainOfThoughtService;

    @PostMapping("/oneshot")
    public Mono<UserChatResponseDto> oneShotChat(@RequestBody UserChatRequestDto userChatRequestDto) {
        return userChatService.getOneShotChat(userChatRequestDto);
    }

    @PostMapping("/oneshot/stream")
    public Flux<UserChatResponseDto> oneShotChatStream(@RequestBody UserChatRequestDto userChatRequestDto) {
        //서비스에서 request가공해서 response돌려줘야함.
        return userChatService.getOneShotChatStream(userChatRequestDto);
    }

    @PostMapping("/cot")
    public Flux<UserChatResponseDto> chainOfThought(@RequestBody UserChatRequestDto userChatRequestDto) {
        //서비스에서 request가공해서 response돌려줘야함.
        return chainOfThoughtService.getChainOfThoughtResponse(userChatRequestDto);
    }
}
