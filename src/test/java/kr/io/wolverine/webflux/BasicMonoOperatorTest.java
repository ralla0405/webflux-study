package kr.io.wolverine.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class BasicMonoOperatorTest {

    @Test
    void startMonoFromData() {
        // just, empty
        Mono.just(1).subscribe(data -> System.out.println("data = " + data));

        // 사소한 에러가 발생했을 때 로그를 남기고 empty의 Mono 전파
        Mono.empty().subscribe(data -> System.out.println("data = " + data));
    }

    @Test
    void startMonoFromFunction() {
        // fromCallable -> 동기적인 객체를 반환할 때 사용
        Mono<String> monoFromCallable = Mono.fromCallable(() -> {
            // 로직 실행
            return callRestTemplate("안녕!");
        }).subscribeOn(Schedulers.boundedElastic()); // <- 기존 톰캣서버로 돌아가는 레거시를 netty기반의 논블로킹 서버로 마이그레이션할 때 사용

        // defer -> Mono를 반환하고 싶을 때 사용
        Mono<String> monoFromDefer = Mono.defer(() -> {
            return callWebClient("안녕!");
        });// <- Mono객체를 Mono객체로 반환, 아래 같은 경우 안녕! 이라는 문자열을 메인 스레드가 완성시킴, 그러나 defer를 사용할 경우 이벤트 루프 스레드가 완성시킴
        monoFromDefer.subscribe();

        Mono<String> monoFromJust = callWebClient("안녕!");
    }

    @Test
    void testDeferNecessity() {
        // abc를 만드는 로직도 Mono의 흐름 안에서 관리하고싶을 때
        // 하나의 큰 흐름을 하나의 Mono안에서 관리하고 싶을 때
        Mono<String> stringMono = Mono.defer(() -> {
            String a = "안녕";
            String b = "하세";
            String c = "요";
            return callWebClient(a + b + c);
        }).subscribeOn(Schedulers.boundedElastic());

    }

    public Mono<String> callWebClient(String request) {
        return Mono.just(request + "callWebClient");
    }

    public String callRestTemplate(String request) {
        return request + "callRestTemplate 응답";
    }

    @Test
    void monoToFlux() {
        // mono에서 데이터 방출의 개수가 많아져서 Flux로 바꾸고 싶을 경우
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data -> Flux.just(data, data + 1, data + 2));
        integerFlux.subscribe(data -> System.out.println("data = " + data));
    }
}
