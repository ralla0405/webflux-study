package kr.io.wolverine.webflux.service.user.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.io.wolverine.webflux.exception.CustomErrorType;
import kr.io.wolverine.webflux.exception.ErrorTypeException;
import kr.io.wolverine.webflux.model.llmclient.LlmChatRequestDto;
import kr.io.wolverine.webflux.model.llmclient.LlmChatResponseDto;
import kr.io.wolverine.webflux.model.llmclient.LlmModel;
import kr.io.wolverine.webflux.model.llmclient.LlmType;
import kr.io.wolverine.webflux.model.llmclient.jsonformat.AnswerListResponseDto;
import kr.io.wolverine.webflux.model.user.chat.UserChatRequestDto;
import kr.io.wolverine.webflux.model.user.chat.UserChatResponseDto;
import kr.io.wolverine.webflux.service.llmclient.LlmWebClientService;
import kr.io.wolverine.webflux.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChainOfThoughtServiceImpl implements ChainOfThoughtService {
    /*
    1단계 : 문제를 이해햐기
    2단계 : 문제를 단계별로 풀어가기
    3단계 : 최종 응답
    범용성 부족!

    1. 사용자의 요청을 효율적으로 분석하기 위한 단계를 LLM에게 물어봄
        -> anwserList: 분석 단계를 LLM이 응답

    2. 분석 단계 별로 LLM에게 요청을 보내어 상세하게 분석

    3. 단계별로 분석된 결과를 종합하여 최종 응답
     */
    private final Map<LlmType, LlmWebClientService> llmWebClientServiceMap;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<UserChatResponseDto> getChainOfThoughtResponse(UserChatRequestDto userChatRequestDto) {
        return Flux.<UserChatResponseDto>create(sink -> {
            String userRequest = userChatRequestDto.request();
            LlmModel requestModel = userChatRequestDto.llmModel();

            String establishingThoughtChainPrompt = String.format("""
                    다음은 사용자의 입력입니다: "%s"
                    사용자에게 체계적으로 답변하기 위해 어떤 단계들이 필요할지 정리해주세요.
                    """, userRequest);

            String establishingThoughtChainSystemPrompt = """
                    아래처럼 List<String> answerList의 형태를 가지는 JSON FORMAT으로 응답해주세요.
                    <JSONSCHEMA>
                    {
                        "answerList": ["", ...]
                    }
                    </JSONSCHEMA>
                    """;

            LlmChatRequestDto llmChatRequestDto = new LlmChatRequestDto(establishingThoughtChainPrompt, establishingThoughtChainSystemPrompt, true, requestModel);

            LlmWebClientService llmWebClientService = llmWebClientServiceMap.get(requestModel.getLlmType());
            Mono<AnswerListResponseDto> cotStepListMono = llmWebClientService.getChatCompletion(llmChatRequestDto)
                    .map(response -> {
                        String llmResponse = response.getLlmResponse();
                        log.info("[COT] cot response : {}" ,llmResponse);
                        String extractedJsonString = ChatUtils.extractJsonString(llmResponse);
                        try {
                            AnswerListResponseDto answerListResponseDto = objectMapper.readValue(extractedJsonString, AnswerListResponseDto.class);
                            //sink.next(new UserChatResponseDto("필요한 작업 단계 분석", answerListResponseDto.toString()));
                            return answerListResponseDto;
                        } catch (JsonProcessingException e) {
                            throw new ErrorTypeException("[JsonParseError] json parse error. extractedJsonString: " + extractedJsonString, CustomErrorType.LLM_RESPONSE_JSON_PARSE_ERROR);
                        }
                    }).doOnNext(publishedData -> sink.next(new UserChatResponseDto("필요한 작업 단계 분석", publishedData.toString())));

            Flux<String> cotStepFlux = cotStepListMono.flatMapMany(cotStepList -> Flux.fromIterable(cotStepList.getAnswerList()));

            Flux<String> analyzedCotStep = cotStepFlux.flatMapSequential(cotStep -> {
                String cotStepRequestPrompt = String.format("""
                        다음은 사용자의 입력입니다: %s
                        
                        사용자의 요구를 다음 단계에 따라 분석해주세요: %s
                        """, userRequest, cotStep);
                return llmWebClientService.getChatCompletionWithCatchException(new LlmChatRequestDto(cotStepRequestPrompt, "", false, requestModel))
                        .map(LlmChatResponseDto::getLlmResponse);
            }).doOnNext(publishedData -> sink.next(new UserChatResponseDto("단계별 분석", publishedData)));

            Mono<String> finalAnswerMono = analyzedCotStep.collectList().flatMap(stepPromptList -> {
                String concatStepPrompt = String.join("\n", stepPromptList);
                String finalAnswerPrompt = String.format("""
                        다음은 사용자의 입력입니다 : %s
                        아래 사항들을 참고, 분석하여 사용자의 입력에 대한 최종 답변을 해주세요:
                        %s
                        """, userRequest, concatStepPrompt);
                return llmWebClientService.getChatCompletionWithCatchException(new LlmChatRequestDto(finalAnswerPrompt, "", false, requestModel))
                        .map(LlmChatResponseDto::getLlmResponse);
            });

            finalAnswerMono.subscribe(finalAnswer -> {
                sink.next(new UserChatResponseDto("최종 응답", finalAnswer));
                sink.complete();
            }, error -> {
                log.error("[COT] cot response error", error);
                sink.error(error);
            });
        });
    }
}
