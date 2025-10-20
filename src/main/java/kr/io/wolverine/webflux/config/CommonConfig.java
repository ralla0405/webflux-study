package kr.io.wolverine.webflux.config;

import kr.io.wolverine.webflux.model.llmclient.LlmType;
import kr.io.wolverine.webflux.service.llmclient.LlmWebClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class CommonConfig {

    @Bean
    public Map<LlmType, LlmWebClientService> getLlmWebClientServiceMap(List<LlmWebClientService> llmWebClientServicesList) {
        return llmWebClientServicesList.stream().collect(Collectors.toMap(LlmWebClientService::getLlmType, Function.identity()));
    }
}
