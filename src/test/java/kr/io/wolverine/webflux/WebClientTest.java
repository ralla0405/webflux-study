package kr.io.wolverine.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class WebClientTest {

    private WebClient webClient = WebClient.builder().build();

    @Test
    void testWebClient() {
        Flux<Integer> intFlux = webClient.get()
                .uri("http://localhost:8080/reactive/onenine/list/flux")
//                .header("Accept", "text/event-stream")
                .retrieve()
                .bodyToFlux(Integer.class);

        intFlux.subscribe(data -> {
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            System.out.println("webflux가 구독 중!!: " + data);
        });

        System.out.println("Netty 이벤트 루프로 스레드 복귀 !!");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
    }
}
