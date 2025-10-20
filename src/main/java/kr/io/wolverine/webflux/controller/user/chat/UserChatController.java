package kr.io.wolverine.webflux.controller.user.chat;

import kr.io.wolverine.webflux.model.user.chat.UserChatRequestDto;
import kr.io.wolverine.webflux.model.user.chat.UserChatResponseDto;
import kr.io.wolverine.webflux.service.user.chat.UserChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class UserChatController {

    private final UserChatService userChatService;

    @PostMapping("/oneshot")
    public Mono<UserChatResponseDto> oneShotChat(@RequestBody UserChatRequestDto userChatRequestDto) {
        return userChatService.getOneShotChat(userChatRequestDto);
    }
}
