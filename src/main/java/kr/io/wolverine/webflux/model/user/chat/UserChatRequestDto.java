package kr.io.wolverine.webflux.model.user.chat;

import kr.io.wolverine.webflux.model.llmclient.LlmModel;

public record UserChatRequestDto(
    String request,
    LlmModel llmModel
) {
}
