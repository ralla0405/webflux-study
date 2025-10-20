package kr.io.wolverine.webflux.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomErrorType {

    GEMINI_RESPONSE_ERROR(1),
    GPT_RESPONSE_ERROR(2),
    LLM_RESPONSE_JSON_PARSE_ERROR(3),
    CHAIN_OF_THOUGHT_RESPONSE_ERROR(4);

    private int code;
}
