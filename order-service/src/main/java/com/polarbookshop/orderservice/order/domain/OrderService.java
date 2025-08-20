package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.book.BookClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

// 이 클래스가 스프링에 의해 관리되는 서비스임을 표시하는 스테레오타입 어노테이션
@Service
public class OrderService {

	private final BookClient bookClient;
	private final OrderRepository orderRepository;

	public OrderService(BookClient bookClient, OrderRepository orderRepository) {
		this.bookClient = bookClient;
		this.orderRepository = orderRepository;
	}

	// 플럭스는 여러개의 주문을 위해 사용된다(0..N)
	public Flux<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	// 주문 객체를 가지고 모노를 생성한다.
	public Mono<Order> submitOrder(String isbn, int quantity) {
		return bookClient.getBookByIsbn(isbn)
				.map(book -> buildAcceptedOrder(book, quantity))
				.defaultIfEmpty(buildRejectedOrder(isbn, quantity))
				// 리액티브 스트림의 앞 단계에서 비동기적으로 생성된 주문 객체를 데이터베이스에 저장한다.
				.flatMap(orderRepository::save);
	}

	public static Order buildAcceptedOrder(Book book, int quantity) {
		return Order.of(book.isbn(), book.title() + " - " + book.author(),
				book.price(), quantity, OrderStatus.ACCEPTED);
	}

	public static Order buildRejectedOrder(String bookIsbn, int quantity) {
		// 주문이 거부되면 isbn, 수량, 상태만 저장한다. 스프링 데이터가 식별자, 버전, 감사 메타데이터를 알아서 처리해준다.
		return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
	}

}
