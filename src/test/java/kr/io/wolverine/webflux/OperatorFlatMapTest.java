package kr.io.wolverine.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OperatorFlatMapTest {
    /*
    Mono<Mono<T>> -> Mono<T>
    Mono<Flux<T>> -> Flux<T>
    Flux<Mono<T>> -> Flux<T>
     */
    @Test
    void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(data -> {
            return Flux.just(data, data + 1, data + 2);
        });
        integerFlux.subscribe(data -> System.out.println("data = " + data));
    }

    @Test
    void testWebClientFlatMap() {
        Flux<String> flatMap = Flux.just(callWebClient("1단계 - 문제 이해하기", 1500),
                callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                callWebClient("3단계 - 최종 응답", 500))
                .flatMap(monoData -> monoData); // <- flatMap은 순서를 보장하지 않는다.

        flatMap.subscribe(data -> System.out.println("flatMapped data = " + data));

        Flux<String> objectFlux = Flux.<Mono<String>>create(sink -> {
            sink.next(callWebClient("1단계 - 문제 이해하기", 1500));
            sink.next(callWebClient("2단계 - 문제 단계별로 풀어가기", 1000));
            sink.next(callWebClient("3단계 - 최종 응답", 500));
            sink.complete();
        }).flatMap(monoData -> monoData); //

        Flux<String> flatMapSequential = Flux.just(callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500))
                .flatMapSequential(monoData -> monoData); // <- flatMap은 순서를 보장하지 않는다.

        flatMapSequential.subscribe(data -> System.out.println("flatMap Sequential data = " + data));

        Flux<String> merge = Flux.merge(callWebClient("1단계 - 문제 이해하기", 1500),
                callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                callWebClient("3단계 - 최종 응답", 500));
//                .map(~~); // <- 여기서 추가로 가공하면 flatMap과 비슷한 구조
        merge.subscribe(data -> System.out.println("merge data = " + data));


        Flux<String> mergeSequential = Flux.mergeSequential(callWebClient("1단계 - 문제 이해하기", 1500),
                callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                callWebClient("3단계 - 최종 응답", 500));
//                .map(~~); // <- 여기서 추가로 가공하면 flatMap과 비슷한 구조
        mergeSequential.subscribe(data -> System.out.println("merge sequential data = " + data));

        // concat, concatMap 쓰지 않는다

        Mono<String> monomonoString = Mono.just(reactor.core.publisher.Mono.just("안녕!")).flatMap(monoData -> monoData);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Flux<Mono<T>>
    Mono<Flux<T>> --> 이 구조 안에 있는 Mono는 flatMap, merge로 벗겨낼 수 있다.
                  --> flatMap, merge 순서를 보장하지 않으니 순서 보장이 필요하면 sequential을 사용
    Mono<Flux<T>> --> flatMapMany --> 얘는 Flux<T> 순서가 보장
    Flux<Flux<T>> --> collectList --> Flux<Mono<List<T>>> --> FLux<List<T>>
     */

    public Mono<String> callWebClient(String request, long delay) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(delay);
                return Mono.just(request + " -> 딜레이 " + delay);
            } catch (Exception e) {
                return Mono.empty();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
