package com.polarbookshop.orderservice.order.event;

import java.util.function.Consumer;

import com.polarbookshop.orderservice.order.domain.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderFunctions {

    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<Flux<OrderDispatchedMessage>> dispatchOrder(OrderService orderService) {
        return flux -> orderService.consumeOrderDispatchedEvent(flux) // 각 발송된 메시지에 대해 데이터베이스의 해당 주문을 업데이트한다.
                .doOnNext(order -> log.info("The order with id {} is dispatched", order.id())) // 데이터베이스에서 업데이트된 각 주문에 대해 로그를 기록한다.
                .subscribe(); // 리액티브 스트림을 활성화하기 위해 구독한다. 구독자가 없으면 스트림을 통해 데이터가 흐르지 않는다.
    }

}
