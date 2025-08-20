package com.polarbookshop.orderservice.config;

import java.net.URI;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

// 사용자 지정 속성 이름의 프리픽스
@ConfigurationProperties(prefix = "polar")
public record ClientProperties (

	@NotNull
	// 카탈로그 서비스의 URI를 지정하는 속성. 널값을 가질 수 없다.
	URI catalogServiceUri

){}
