package kr.io.wolverine.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicFluxOperatorTest {

    /**
     * Flux
     * 데이터: just, empty, from~시리즈
     * 함수: defer, create
     */
    @Test
    void startFluxFromData() {
        Flux.just(1, 2, 3, 4)
                .subscribe(data -> System.out.println("data = " + data));

        List<Integer> basicList = List.of(1, 2, 3, 4, 5);
        Flux.fromIterable(basicList)
                .subscribe(data -> System.out.println("data fromIterable = " + data));
    }

    /**
     * Flux defer -> 안에서 Flux 객체를 반환
     * Flux create -> 안에서 동기적인 객체를 반환
     */
    @Test
    void startFluxFromFunction() {
        Flux.defer(() -> {
            return Flux.just(1, 2, 3, 4);
        }).subscribe(data -> System.out.println("data from defer = " + data));

        Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete(); // 마지막을 알려주는 함수 호출
        }).subscribe(data -> System.out.println("data from sink= " + data));
    }

    @Test
    void testSinkDetail() {
        Flux.<String>create(sink -> {
            AtomicInteger counter = new AtomicInteger(0);
            recursiveFunction(sink, counter);
        }).subscribe(data -> System.out.println("data = " + data));
    }

    public void recursiveFunction(FluxSink<String> sink, AtomicInteger counter) {
        if (counter.incrementAndGet() < 10) {
            sink.next("sink count " + counter);
            recursiveFunction(sink, counter);
        } else {
            sink.complete();
        }
    }

    // ThreadLocal -> context
    @Test
    void testSinkContext() {
        Flux.<String>create(sink -> {
            recursiveFunctionContext(sink);
        }).contextWrite(Context.of("counter", new AtomicInteger(0)))
        .subscribe(data -> System.out.println("data = " + data));
    }

    public void recursiveFunctionContext(FluxSink<String> sink) {
        AtomicInteger counter = sink.contextView().get("counter");
        if (counter.incrementAndGet() < 10) {
            sink.next("sink count " + counter);
            recursiveFunction(sink, counter);
        } else {
            sink.complete();
        }
    }

    @Test
    void testBasicCollectList() {
        // Flux를 Mono로 변경할 수 있다
        Mono<List<Integer>> listMono = Flux.<Integer>just(1, 2, 3, 4, 5)
                .map(data -> data * 2)
                .filter(data -> data % 4 == 0)
                .collectList();

        listMono.subscribe(data -> System.out.println("Flux가 구독한 data = " + data));
    }
}
