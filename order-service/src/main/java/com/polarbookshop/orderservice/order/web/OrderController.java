package com.polarbookshop.orderservice.order.web;

import javax.validation.Valid;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 클래스가 스프링 컴포넌트임을 표시하는 스테레오타입 어노테이션. REST 엔드포인트에 대한 핸들러가 정의되는 클래스임을 나타낸다.
@RestController

// 클래스가 핸ㄷ르러를 제공하는 URI의 루트 패스(/orders)를 식별한다.
@RequestMapping("orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	// Flux는 여러 개의 객체를 위해 사용된다.
	public Flux<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@PostMapping
	public Mono<Order> submitOrder(
			// OrderRequest 객체를 받아서 유효성 검증을 하고 주문을 생성한다. 생성한 주문은 모노로 반환한다.
			@RequestBody @Valid OrderRequest orderRequest) {
		return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
	}

}
