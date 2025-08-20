package com.polarbookshop.orderservice.book;

import java.time.Duration;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class BookClient {

	private static final String BOOKS_ROOT_API = "/books/";
	// 이전에 설정된 WebClient 빈
	private final WebClient webClient;

	public BookClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public Mono<Book> getBookByIsbn(String isbn) {
		return webClient
				.get() // 요청은 get 메서드 사용
				.uri(BOOKS_ROOT_API + isbn) // 요청 uri는 /books/{isbn}이다.
				.retrieve() // 요청을 보내고 응답을 받는다.
				.bodyToMono(Book.class) // 받은 객체를 mono<book>으로 반환한다.
				.timeout(Duration.ofSeconds(3), Mono.empty())
				.onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
				.retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
				.onErrorResume(Exception.class, exception -> Mono.empty());
	}

}
