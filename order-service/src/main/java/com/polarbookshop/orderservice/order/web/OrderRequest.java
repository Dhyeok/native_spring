package com.polarbookshop.orderservice.order.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// 유효성 검증 제약 조건이 정의된 OrderRequest DTO 클래스
public record OrderRequest (

	@NotBlank(message = "The book ISBN must be defined.")
	String isbn, // 널값을 가질 수 없고 최소한 화이트 스페이스가 아닌 문자를 하나 이상 가져야 한다.

	@NotNull(message = "The book quantity must be defined.")
	@Min(value = 1, message = "You must order at least 1 item.")
	@Max(value = 5, message = "You cannot order more than 5 items.")
	Integer quantity // 널값을 가질 수 없고 1부터 5사이의 값을 가져야 한다.

){}
