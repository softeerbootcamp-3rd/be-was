# java-was-2023
Java Web Application Server 2023   

## 프로젝트 설계
<img src="https://github.com/jiaeYoon/softeer3-be-was/assets/68904755/5447b55a-0cd5-4cfe-b1bd-a2c4ed9a594c" width="90%"/>

# 1단계

### 요구사항
- GET /index.html 응답하기
- HTTP Request 내용 출력
- Concurrent 패키지 사용
- 유지보수에 좋은 구조에 대해 고민하고 코드 개선하기

### 체크리스트
- [x] http://localhost:8080/index.html 페이지에 접근 가능하다.
- [x] Java Thread 기반으로 작성된 프로젝트를 Concurrent 패키지를 사용하도록 변경한다.
- [x] 기능 요구사항 만족 후 리팩토링을 진행한다.

### 학습 내역
- WAS
- HTTP
- 스레드와 스레드 풀
- OOP
- 클린 코드


# 2단계
### 요구사항
- GET으로 회원가입 기능 구현하기
- 유지보수가 편한 코드가 되도록 코드품질을 개선하기
- Junit을 활용한 단위 테스트 작성하기

### 체크리스트
- [x] 회원가입 기능 구현
    - [x] 회원가입 데이터 유효성 검증
    - [x] 쿼리스트링의 value 데이터 디코딩
    - [x] 쿼리스트링을 User 객체의 필드에 동적 바인딩
    - [x] 회원가입 예외처리
    - [x] 단위테스트 작성

### 학습 내역
- [GET 요청의 동작 과정](https://github.com/jiaeYoon/softeer3-be-was/wiki/HTTP-%7C-GET)
- [Java Reflection](https://github.com/jiaeYoon/softeer3-be-was/wiki/JAVA-%7C-Reflection)
- Controller의 역할

# 3단계
### 요구사항
- 다양한 컨텐츠 타입을 지원하도록 코드 개선하기

### 체크리스트
- [x] css, js, favicon 등의 컨텐츠 타입 적용
- [x] style.css 수정

### 학습 내역
- [HTTP Response와 MIME Type](https://github.com/jiaeYoon/softeer3-be-was/wiki/HTTP-%7C-HTTP-Response%EC%99%80-MIME-Type)

# 4단계
### 요구사항
- 회원가입을 GET에서 POST로 수정 후 정상 동작하도록 구현하기 
- 가입을 완료하면 /index.html 페이지로 이동하기
- 불필요한 외부 의존성 제거(자바 기본 패키지, Junit, AssertJ, Logger 외의 외부 패키지 사용 X)
- java.nio 에서 java.io로 변환

### 체크리스트
- [x] Request Body 추출
- [x] HTTP Method를 GET -> POST으로 변경 후 회원가입이 동작하도록 코드 수정
- [x] Redirect 구현
- [x] java.nio에서 java.io로 변환
  - [x] 파일 내용을 읽어 byte[]로 변환
  - [x] 파일의 mimeType 도출
  - [x] 이진 형태의 문자를 디코딩

### 학습 내역
- [java.io와 java.nio](https://github.com/jiaeYoon/softeer3-be-was/wiki/JAVA-%7C-java.io%EC%99%80-java.nio)
- redirect
- BufferedReader의 read()와 readline()

# 5단계
### 요구사항
- 로그인 기능 구현
- 로그인 성공 시, index.html로 이동 + HTTP 헤더의 쿠키 값을 SID=세션 ID로 응답
- 로그인 실패 시, /user/login_failed.html로 이동

### 체크리스트
- [x] 로그인 기능 구현
- [x] 로그인 성공 시, index.html로 리다이렉트
- [x] 로그인 실패 시, /user/login_failed.html로 리다이렉트
- [x] 로그인 성공 시, 쿠키 값에 세션 ID를 넣어 반환
- [x] 요청한 파일이 없는 경우, 404 Not Found 페이지 응답

### 학습 내역
- 쿠키와 세션
- Dispatcher Servlet의 동작 방식
- Reflection Method Invoke

<hr>

## 프로젝트 정보 
이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.