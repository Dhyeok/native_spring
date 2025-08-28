package com.polarbookshop.orderservice.order.web;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import com.polarbookshop.orderservice.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest(OrderController.class) // ordercontroller를 대상으로 한 스프링 웹플럭스 컴포넌트에 집중하는 테스트 클래스임을 나타낸다.
class OrderControllerWebFluxTests {

	@Autowired
	private WebTestClient webClient; // 웹클라이언트의 변현으로 restgul 서비스 테스트를 수비게 하기 위한 기능을 추가로 가지고있따.

	@MockBean
	private OrderService orderService; //  orderservice의 모의 객체를 스프링 애플리케이션 콘텍스트에 추가한다.

	@Test
	void whenBookNotAvailableThenRejectOrder() {
		var orderRequest = new OrderRequest("1234567890", 3);
		var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());
		given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
				.willReturn(Mono.just(expectedOrder)); // order service 모의 빈이 어떻게 작동해야 하는지 지정한다.

		webClient
				.post()
				.uri("/orders")
				.bodyValue(orderRequest)
				.exchange()
				.expectStatus().is2xxSuccessful() // 주문이 성공적으로 생성될 것을 예상한다.
				.expectBody(Order.class).value(actualOrder -> {
					assertThat(actualOrder).isNotNull();
					assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
				});

	}

}
