package com.polarbookshop.edgeservice.config;

import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

	@Bean
	public KeyResolver keyResolver() {
		return exchange -> Mono.just("anonymous");
	} // 요청에 대한 사용률 제한은 상수 키를 사용해 적용한다.
	
}
