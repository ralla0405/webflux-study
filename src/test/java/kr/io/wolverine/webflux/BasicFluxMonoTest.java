package kr.io.wolverine.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class BasicFluxMonoTest {

    @Test
    void testBasicFluxMono() {
        // Flux -> 0개 이상의 데이터를 방출할 수 있는 객체 -> List, Stream
        // 1. 빈 함수
        // 2. 데이터로부터 시작
        Flux.<Integer>just(1, 2, 3, 4, 5)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Flux가 구독한 data = " + data));

        // Mono -> 0개부터 1개의 데이터만 방출할 수 있는 객체 -> Optional 정도
        Mono.<Integer>just(2)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .subscribe(data -> System.out.println("Mono가 구독한 data = " + data));
    }

    @Test
    void testFluxMonoBlock() {
        Mono<String> justString = Mono.just("스트링");
        String string = justString.block(); // <- String 으로 바뀜, 말 그대로 블록을 하는거라 사용안하는게 정석
        System.out.println("string = " + string);
    }
}
