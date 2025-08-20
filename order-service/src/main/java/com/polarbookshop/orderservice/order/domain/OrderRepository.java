package com.polarbookshop.orderservice.order.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// CRUD 연산을 제공하는 리액티브 리포지터리가 관리할 엔티티의 유형(order)과 해당 엔티티의 기본키 유형(long)을 지정하고 확장한다.
public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
}
