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
	// 카탈로그 서비스를 호출해 책의 주문 가능성을 확인
	public Mono<Order> submitOrder(String isbn, int quantity) {
		// 책 주문이 가능하면 접수한다.
		return bookClient.getBookByIsbn(isbn)
				.map(book -> buildAcceptedOrder(book, quantity))
				// 책이 카탈로그에 조냊하지 않으면 주문을 거부
				.defaultIfEmpty(buildRejectedOrder(isbn, quantity))
				// 리액티브 스트림의 앞 단계에서 비동기적으로 생성된 주문 객체를 데이터베이스에 저장한다.
				.flatMap(orderRepository::save);
	}

	public static Order buildAcceptedOrder(Book book, int quantity) {
		return Order.of(book.isbn(), book.title() + " - " + book.author(),
				book.price(), quantity, OrderStatus.ACCEPTED); // 주문이 접수되면 isbn, 책의 이름(제목과 저자), 수량, 상태만 지정하면 스프링 데이터가 식별자, 버전, 감사 메타데이터를 추가한다.
	}

	public static Order buildRejectedOrder(String bookIsbn, int quantity) {
		// 주문이 거부되면 isbn, 수량, 상태만 저장한다. 스프링 데이터가 식별자, 버전, 감사 메타데이터를 알아서 처리해준다.
		return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
	}

}
