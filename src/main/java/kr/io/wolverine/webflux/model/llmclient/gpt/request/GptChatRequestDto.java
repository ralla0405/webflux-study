package kr.io.wolverine.webflux.model.llmclient.gpt;

import kr.io.wolverine.webflux.model.llmclient.LlmModel;

import java.util.List;

public record GptChatRequestDto(
        List<GptCompletionRequestDto> messages,
        LlmModel model,
        Boolean stream
) {
}
