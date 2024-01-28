# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 2주차 계획

### 단계별 기능

+ 4단계 [POST 메서드 구분 및 HTTP Body값 파싱]
  + HTTP 메서드별로 Controller 구분 기능 구현
  + HTTP Body값을 특정 Custom 객체로 Mapping 기능 구현
  + 기존 MyHttpServletRequest에 HTTP Body값을 저장할 수 있도록 수정
+ 5단계 [쿠키 세션 로그인]
  + in-memory 세션 구현 
  + 로그인 성공 시 Cookie 발급 & HTTP 응답 헤더에 담을 수 있는 기능 구현 
+ 6단계 [동적인 HTML 구현]
  + HTML 동적으로 build하는 기능 구현 (StringBuilder)

### 공부 할 것

+ 쿠키 & 세션의 자료구조
+ 기존 서버(tomcat etc)의 Session 관리 방식
+ filter, interceptor 사용한 인증/인가
+ 동적으로 HTML 빌드하기

### 계획

+ 1/22

  Body값을 특정 Custom객체에 Mapping 기능 (ParameterMapper, MyHttpServletRequest 수정)
  
  HTTP Method를 구분하는 기능 (annotation 추가 혹은 내부 Value추가)

+ 1/23
  
  쿠키 & 세션 내부 자료 구조 및 주의 사항 공부.
  
  Session 구현
+ 1/24

  인증/인가 기능 공부, Cookie 발급 기능 구현.

  STEP4 기능 단위 테스트
+ 1/25
  
  Controller에서 Parameter로 Cookie value 자유롭게 받을 수 있는 기능 구현

  동적인 HTML 빌드하는 기능 공부 및 구현

  인증/인가 기능 설계
+ 1/26

  동적인 HTML 빌드하는 기능 공부 및 구현