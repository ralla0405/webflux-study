package kr.io.wolverine.webflux.model.llmclient;

import kr.io.wolverine.webflux.model.user.chat.UserChatRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LlmChatRequestDto {

    private String userRequest;
    private String systemPrompt;
    private boolean useJson;
    private LlmModel llmModel;

    public LlmChatRequestDto(UserChatRequestDto userChatRequestDto, String systemPrompt) {
        this.userRequest = userChatRequestDto.request();
        this.systemPrompt = systemPrompt;
        this.llmModel = userChatRequestDto.llmModel();
    }
}
