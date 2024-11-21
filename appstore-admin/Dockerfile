FROM openjdk:21-jdk-slim

# JAR_FILE 변수 정의
ARG JAR_FILE=/build/libs/*.jar

# JAR 파일을 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 프로파일을 prod로 설정
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

# 시스템 진입점 정의
ENTRYPOINT ["java", "-jar", "/app.jar"]
