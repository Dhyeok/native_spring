package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.config.PolarProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // REST/HTTP 엔드포인트를 위한 핸들러를 정의하는 클래스로 인식
public class HomeController {

	/*
	@GetMapping("/") // root엔드포인트로 GET 요청을 처리
	public String getGreeting() {
		return "Welcome to the book catalog!";
	}
    */

	private final PolarProperties polarProperties; // 사용자 정의 속성 액세스를 위해 생성자 오토와이어링을 통해 주입된 빈

	public HomeController(PolarProperties polarProperties) {
		this.polarProperties = polarProperties;
	}

	@GetMapping("/")
	public String getGreeting() {
		return polarProperties.getGreeting(); // 설정 데이터 빈에서 가져온 환영 메시지를 사용
	}

}
