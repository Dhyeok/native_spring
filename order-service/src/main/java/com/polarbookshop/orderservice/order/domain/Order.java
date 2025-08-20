
package com.polarbookshop.orderservice.order.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table("orders") // order 객체와 order 테이블 사이의 매핑 설정
public record Order (

	@Id
	Long id, // 엔티티의 기본키

	String bookIsbn,
	String bookName,
	Double bookPrice,
	Integer quantity,
	OrderStatus status,

	@CreatedDate
	Instant createdDate, // 엔티티가 생성된 시기

	@LastModifiedDate
	Instant lastModifiedDate, // 엔티티가 최종 수정된 시기

	@Version
	int version // 엔티티의 버전 번호
){

	public static Order of(String bookIsbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status) {
		return new Order(null, bookIsbn, bookName, bookPrice, quantity, status, null, null, 0);
	}

}
