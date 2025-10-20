package kr.io.wolverine.webflux.model.llmclient.gpt;

public record GptCompletionRequestDto(
        GptMessageRole role,
        String content
) {
}
