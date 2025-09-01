package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.book.BookClient;
import com.polarbookshop.orderservice.order.event.OrderAcceptedMessage;
import com.polarbookshop.orderservice.order.event.OrderDispatchedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 이 클래스가 스프링에 의해 관리되는 서비스임을 표시하는 스테레오타입 어노테이션
@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final BookClient bookClient;
	private final OrderRepository orderRepository;
	private final StreamBridge streamBridge;

	public OrderService(BookClient bookClient, StreamBridge streamBridge, OrderRepository orderRepository) {
		this.bookClient = bookClient;
		this.orderRepository = orderRepository;
		this.streamBridge = streamBridge;
	}

	// 플럭스는 여러개의 주문을 위해 사용된다(0..N)
	public Flux<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	// 주문 객체를 가지고 모노를 생성한다.
	// 카탈로그 서비스를 호출해 책의 주문 가능성을 확인
	@Transactional // 메서드를 로컬 트랜잭션으로 실행한다.
	public Mono<Order> submitOrder(String isbn, int quantity) {
		// 책 주문이 가능하면 접수한다.
		return bookClient.getBookByIsbn(isbn)
				.map(book -> buildAcceptedOrder(book, quantity))
				// 책이 카탈로그에 조냊하지 않으면 주문을 거부
				.defaultIfEmpty(buildRejectedOrder(isbn, quantity))
				// 리액티브 스트림의 앞 단계에서 비동기적으로 생성된 주문 객체를 데이터베이스에 저장한다.
				.flatMap(orderRepository::save)
				.doOnNext(this::publishOrderAcceptedEvent); // 주문이 접수되면 이벤트를 발행한다.
	}

	public static Order buildAcceptedOrder(Book book, int quantity) {
		return Order.of(book.isbn(), book.title() + " - " + book.author(),
				book.price(), quantity, OrderStatus.ACCEPTED); // 주문이 접수되면 isbn, 책의 이름(제목과 저자), 수량, 상태만 지정하면 스프링 데이터가 식별자, 버전, 감사 메타데이터를 추가한다.
	}

	public static Order buildRejectedOrder(String bookIsbn, int quantity) {
		// 주문이 거부되면 isbn, 수량, 상태만 저장한다. 스프링 데이터가 식별자, 버전, 감사 메타데이터를 알아서 처리해준다.
		return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
	}

	private void publishOrderAcceptedEvent(Order order) {
		if (!order.status().equals(OrderStatus.ACCEPTED)) {
			return; // 주문의 상태가 ACCEPTED가 아니면 아무것도 하지 않는다.
		}
		var orderAcceptedMessage = new OrderAcceptedMessage(order.id()); // 주문이 접수된 것을 알리는 메시지를 생성한다.
		log.info("Sending order accepted event with id: {}", order.id());
		var result = streamBridge.send("acceptOrder-out-0", orderAcceptedMessage); // 메시지를 acceptOrder-ou-0  바인딩에 명시적으로 보낸다.
		log.info("Result of sending data for order with id {}: {}", order.id(), result);
	}

	public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) { // OrderDispatchedMessage의 리액티브 스트림을 입력으로 받는다.
		return flux
				.flatMap(message -> orderRepository.findById(message.orderId())) // 스트림으로 보낸 각 객체에 대해 데이터베이스에서 해당 주문을 읽어온다.
				.map(this::buildDispatchedOrder) // 주문의 상태를 '배송됨'으로 업데이트 한다.
				.flatMap(orderRepository::save); // 업데이트된 주문을 데이터베이스에 저장한다.
	}

	private Order buildDispatchedOrder(Order existingOrder) {
		return new Order( // 주어진 주문에 대해 '배송됨' 상태를 갖는 새로운 주문 레코드를 반환한다.
				existingOrder.id(),
				existingOrder.bookIsbn(),
				existingOrder.bookName(),
				existingOrder.bookPrice(),
				existingOrder.quantity(),
				OrderStatus.DISPATCHED,
				existingOrder.createdDate(),
				existingOrder.lastModifiedDate(),
				existingOrder.version()
		);
	}

}
