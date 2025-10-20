package kr.io.wolverine.webflux.model.llmclient.gpt.response;

public record GptChoice(
        String finish_reason,
        GptResponseMessageDto message,
        GptResponseMessageDto delta
) {
}
