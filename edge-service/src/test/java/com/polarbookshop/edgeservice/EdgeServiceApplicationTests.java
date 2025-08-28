package com.polarbookshop.edgeservice;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

// 완전한 스프링 웹 어플리케이션 콘텍스트와 임의의 포트에서 듣는 웹 환경을 로드한다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers // 테스트 컨테이너의 자동 시작과 종료를 활성화
class EdgeServiceApplicationTests {

	private static final int REDIS_PORT = 6379;

	@Container
	static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2")) // 테스트를 위한 레디스 컨테이너 정의
			.withExposedPorts(REDIS_PORT);

	@DynamicPropertySource // 테스트 인스턴스를 사용하도록 레디스 설정 변경
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redis.getHost());
		registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));
	}

	@Test
	void verifyThatSpringContextLoads() { // 애플리케이션 콘텍스트가 올바르게 로드되었는지, 레디스 연결이 성공적으로 됐는지를 확인하기 위한 테스트로 비어있따.
	}

}

/**
 @SpringBootTest(webEnvironment = RANDOM_PORT)	스프링 컨텍스트를 테스트용 서버와 함께 로드
 @Testcontainers	Testcontainers 자동 시작/종료 활성화
 @Container	개별 컨테이너 선언
 GenericContainer<?>	Docker 컨테이너를 임의로 실행
 DockerImageName.parse("redis:7.2")	사용할 Docker 이미지 지정
 .withExposedPorts(6379)	컨테이너 포트 공개
 @DynamicPropertySource	테스트 실행 중 동적 환경 변수 등록
 registry.add("spring.redis.host", ...)	Redis 호스트 지정
 registry.add("spring.redis.port", ...)	Redis 포트 지정
 @Test	JUnit 5 테스트 메서드
 verifyThatSpringContextLoads()	스프링 컨텍스트가 정상 로드되는지 확인
  **/
