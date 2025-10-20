package kr.io.wolverine.webflux.model.llmclient;

import kr.io.wolverine.webflux.model.llmclient.gpt.response.GptChatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LlmChatResponseDto {

    private String llmResponse;

    public LlmChatResponseDto(GptChatResponseDto gptChatResponseDto) {
        this.llmResponse = gptChatResponseDto.getSingleChoice().message().content();
    }

}
