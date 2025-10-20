package kr.io.wolverine.webflux.model.llmclient.gpt.response;


import kr.io.wolverine.webflux.exception.CustomErrorType;
import kr.io.wolverine.webflux.exception.ErrorTypeException;

import java.util.List;

public record GptChatResponseDto(
        List<GptChoice> choices
) {

    public GptChoice getSingleChoice() {
        return choices.stream().findFirst().orElseThrow(() ->
                new ErrorTypeException("[GptResponse] There is no choices.", CustomErrorType.GPT_RESPONSE_ERROR));
    }
}
