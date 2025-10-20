package kr.io.wolverine.webflux.model.user.chat;

import kr.io.wolverine.webflux.exception.CommonError;
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

    private String title;
    private String response;
    private CommonError error;

    public UserChatResponseDto(String title, String response) {
        this.title = title;
        this.response = response;
    }

    public UserChatResponseDto(LlmChatResponseDto llmChatResponseDto) {
        this.title = llmChatResponseDto.getTitle();
        this.response = llmChatResponseDto.getLlmResponse();
        this.error = llmChatResponseDto.getError();
    }
}
