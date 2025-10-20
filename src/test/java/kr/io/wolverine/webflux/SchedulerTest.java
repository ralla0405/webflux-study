package kr.io.wolverine.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class SchedulerTest {

    @Test
    void testBasicFluxMono() {
        Mono.<Integer>just(2)
                .map(data -> {
                    System.out.println("map Thread name = " + Thread.currentThread().getName());
                    return data * 2;
                })
                .publishOn(Schedulers.parallel())
                .filter(data -> {
                    System.out.println("filter Thread name = " + Thread.currentThread().getName());
                    return data % 4 == 0;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(data -> System.out.println("Mono가 구독한 data! " + data));
    }
}
