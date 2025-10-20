package kr.io.wolverine.webflux.model.llmclient.jsonformat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerListResponseDto {
    private List<String> answerList;

    @Override
    public String toString() {
        return String.join("\n\n", answerList);
    }
}
