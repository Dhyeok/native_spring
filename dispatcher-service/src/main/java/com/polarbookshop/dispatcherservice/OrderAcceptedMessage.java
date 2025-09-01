package com.polarbookshop.dispatcherservice;

public record OrderAcceptedMessage ( // 주문의 아이디를 Long 타입의 필드에 가지고 있는 DTO
		Long orderId
){}
