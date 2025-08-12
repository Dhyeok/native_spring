FROM eclipse-temurin:17 AS builder # JRE가 이미 설치되어 있는 이클립스 테무린 배포판 우분투 베이스 이미지
WORKDIR workspace # 현재 작업폴더를 workspace로 변경
ARG JAR_FILE=build/libs/*.jar # 프로젝트에서 애플리케이션 jar파일의 위치를 지정하는 빌드 인수
COPY ${JAR_FILE} catalog-service.jar # 애플리케이션 jar 파일을 로컬 머신에서 이미지 안으로 복사
RUN java -Djarmode=layertools -jar catalog-service.jar extract  # 애플리케이션을 실행하기 위한 컨테이너 진입점을 지정

FROM eclipse-temurin:17
RUN useradd spring
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]