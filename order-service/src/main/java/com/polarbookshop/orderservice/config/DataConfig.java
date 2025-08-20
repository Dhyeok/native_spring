package com.polarbookshop.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration // 이 클래스가 스프링 설정을 위한 클래스임을 나타냄
@EnableR2dbcAuditing // db 감사 빈(타임스탬프 등)
public class DataConfig { // 지속성 엔티티에 대한 R2DBC 감사를 활성화
}
