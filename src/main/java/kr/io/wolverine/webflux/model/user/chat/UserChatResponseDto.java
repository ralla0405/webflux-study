package kr.io.wolverine.webflux.model.user.chat;

import kr.io.wolverine.webflux.model.llmclient.LlmChatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChatResponseDto {
    private String response;

    public UserChatResponseDto(LlmChatResponseDto llmChatResponseDto) {
        this.response = llmChatResponseDto.getLlmResponse();
    }
}
