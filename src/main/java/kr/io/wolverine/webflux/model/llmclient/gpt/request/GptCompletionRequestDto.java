package kr.io.wolverine.webflux.model.llmclient.gpt.request;

import kr.io.wolverine.webflux.model.llmclient.gpt.GptMessageRole;

public record GptCompletionRequestDto(
        GptMessageRole role,
        String content
) {
}
