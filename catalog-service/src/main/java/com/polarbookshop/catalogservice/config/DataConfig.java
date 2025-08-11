package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration // 이 클래스가 스프링 설정의 소스임을 나타낸다.
@EnableJdbcAuditing // 지속성 엔티티에 대한 감사를 활성화
public class DataConfig {
}
