package com.polarbookshop.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/* 스프링 설정 클래스를 정의하고 컴포넌트 스캔과 스프링 부트 자동 설정을 실행한다.
 위 어노테이션은 아래와 같은 3가지 다른 어노테이션을 한꺼번에 포함
 @Configuration은 해당 클래스가 빈을 정의하는 클래스임을 나타냄
 @ComponentScan을 사용하면 컴포넌트 검색을 통해 빈을 찾아 스프링 콘텍스트에 자동으로 등록
 @EnableAutoConfiguration은 스프링 부트에서 제공하는 자동 설정 기능을 활성화
*/

public class CatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

}
