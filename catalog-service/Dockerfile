# JRE가 이미 설치되어 있는 이클립스 테무린 배포판 우분투 베이스 이미지
FROM eclipse-temurin:17 AS builder
# 현재 작업폴더를 workspace로 변경
WORKDIR workspace
# 프로젝트에서 애플리케이션 jar파일의 위치를 지정하는 빌드 인수
ARG JAR_FILE=build/libs/*.jar
# 애플리케이션 jar 파일을 로컬 머신에서 이미지 안으로 복사
COPY ${JAR_FILE} catalog-service.jar

# 계층 JAR 모드를 적용해 아카이브에서 계층을 추출한다.
RUN java -Djarmode=layertools -jar catalog-service.jar extract


# 두번째 단계를 위한 openjdk 베이스 이미지
FROM eclipse-temurin:17
RUN useradd spring
USER spring
WORKDIR workspace
# 첫번째 단계에서 추출한 jar 계층을 두번째 단계로 복사한다.
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
# 스프링 부트 런쳐를 사용해 우버 jar 대신 계층으로 애플리케이션을 시작한다.
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]