package com.polarbookshop.dispatcherservice;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 함수는 설정 클래스에서 정의된다.
public class DispatchingFunctions {

	private static final Logger log = LoggerFactory.getLogger(DispatchingFunctions.class);

	@Bean // 빈으로 정의된 함수는 스프링 클라우드 함수가 찾아서 관리할 수 있다.
	public Function<OrderAcceptedMessage, Long> pack() { // 주문을 포장하는 비즈니스 로직을 구현하는 함수
		return orderAcceptedMessage -> { // OrderAccepteMessage 객체를 입력으로 받는다.
			log.info("The order with id {} is packed.", orderAcceptedMessage.orderId());
			return orderAcceptedMessage.orderId(); // 주문의 식별자(Long 타입)를 출력으로 반환한다.
		};
	}

	@Bean // 주문 레이블링 비즈니스 로직을 구현하는 함수
	public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
		return orderFlux -> orderFlux.map(orderId -> { // 주문의 아이디(Long 타입)을 입력으로 받는다.
			log.info("The order with id {} is labeled.", orderId);
			return new OrderDispatchedMessage(orderId); // 출력으로 OrderDispatchedMessage를 반환한다.
		});
	}

}
