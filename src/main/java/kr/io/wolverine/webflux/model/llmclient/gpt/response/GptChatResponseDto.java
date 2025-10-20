package kr.io.wolverine.webflux.model.llmclient.gpt.response;


import java.util.List;

public record GptChatResponseDto(
        List<GptChoice> choices
) {

    public GptChoice getSingleChoice() {
        return choices.stream().findFirst().orElseThrow();
    }
}
