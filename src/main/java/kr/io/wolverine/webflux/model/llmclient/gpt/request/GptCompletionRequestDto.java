package kr.io.wolverine.webflux.model.llmclient.gpt.request;

public record GptCompletionRequestDto(
        GptMessageRole role,
        String content
) {
}
