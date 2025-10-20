package kr.io.wolverine.webflux.model.llmclient.gpt.request;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GptMessageRole {
    SYSTEM,
    USER,
    ASSISTANT
    ;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
