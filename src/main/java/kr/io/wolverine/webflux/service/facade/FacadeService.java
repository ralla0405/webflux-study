package kr.io.wolverine.webflux.service.facade;

import kr.io.wolverine.webflux.model.facade.FacadeHomeResponseDto;
import reactor.core.publisher.Mono;

public interface FacadeService {

    Mono<FacadeHomeResponseDto> getFacadeHomeResponseDto();
}
