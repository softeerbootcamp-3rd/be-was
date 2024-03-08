# softeer3-java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 
https://github.com/sjmjys954646/be-was/tree/feat7

## 목표
처음에 설계에 노력을 들여 구상한 나만의 WAS 구조 방식을 크게 변경하지 않고 완성시켜보자

## 프로젝트 구조도
```
.
├── main
│   ├── java
│   │   ├── db
│   │   │   ├── Database.java
│   │   │   ├── H2Database.java
│   │   │   ├── PostRepository.java
│   │   │   ├── SessionManager.java
│   │   │   ├── UserRepository.java
│   │   │   └── dto
│   │   ├── model
│   │   │   ├── Post.java
│   │   │   └── User.java
│   │   ├── utils
│   │   │   ├── LoginChecker.java
│   │   │   ├── RequestBodyParse
│   │   │   └── UserFormDataParser.java
│   │   └── webserver
│   │       ├── HttpConnectionHandler.java
│   │       ├── WebServer.java
│   │       └── http
│   └── resources
│       ├── logback.xml
│       ├── static
│       │   ├── css
│       │   ├── favicon.ico
│       │   ├── fonts
│       │   ├── images
│       │   └── js
│       └── templates
│           ├── error
│           ├── index.html
│           ├── post
│           ├── qna
│           └── user
└── test
    ├── java
    │   │
    │   └── webserver
    │       ├── LoginTest.java
    │       └── http
    └── resources
        └── logback-test.xml

```


## 1주차 개발과정 및 학습과정 
### 웹 서버 1단계 - index.html 응답 
#### 요구사항
- HTTP 학습 후 웹서버 구현
- Java 멀티스레드 프로그래밍
- 유지보수가 좋은 코드
- index.html접속시 파일을 읽어 클라이언트에 응답

#### 개발과정 
##### 1. WAS란 무엇인가에 대한 공부
##### 2. HTTP Response, Request 설계
##### 3. JAVA에서 멀티쓰레드에 대해 공부

### 웹 서버 2단계 - GET으로 회원가입 
#### 요구사항
- GET프로토콜 이해 
- Junit 이용한 테스트코드 작성 

#### 개발과정
##### 4. ResponseBody의 변경 
##### 5. Redirect를 위한 상태코드 처리 
##### 6. 테스트코드 작성 

### 웹 서버 3단계 - 다양한 컨텐츠 타입 지원
#### 요구사항
- 지원할 컨텐츠 타입 확장

#### 개발과정
##### 7. MIME타입 추가

#### 회고 
- [WAS란?](https://b1ackhand.tistory.com/260)
- [WAS 설계 과정](https://b1ackhand.tistory.com/262)
- [Git에 대한 진실](https://b1ackhand.tistory.com/261)
- [멀티쓰레드프로그래밍](https://b1ackhand.tistory.com/263)

## 2주차 개발과정 및 학습과정
### 웹 서버 4단계 - POST로 회원 가입
#### 요구사항
- nio 대신 io 사용
- POST 요청

#### 개발과정
##### 8. NIO와 IO의 차이 학습
##### 9. BODY파싱을 위한 팩토리패턴

### 웹 서버 5단계 - 쿠키를 이용한 로그인
#### 요구사항
- 회원가입 후 로그인 성공 및 실패 판단
- 로그인에 성공시 세션값으로 응답

#### 개발과정
##### 10. 쿠키와 세션이란?

### 웹 서버 6단계 - 동적인 HTML
#### 요구사항
- 사용자가 로그인 상태일 경우 아닐 경우 표시
- 사용자가 로그인 상태일 경우 사용자 목록을 출력 아닐 경우 redirect

#### 개발과정
##### 11. HTML동적핸들러 생성
##### 12. 데이터베이스 학습

## 3주차 개발과정 및 학습과정
### 웹 서버 7단계 - 동적인 HTML
#### 요구사항
- 글쓰기 기능 구현
- 세부 글 표시
- 에러 페이지 구현
- db 연결

#### 개발과정
##### 13. H2 데이터베이스 연결
##### 14. 글씨기 POST 요청 및 세부 페이지 GET 요청 구현 
##### 15. 에러페이지 구현

#### 회고 
- [IO와 NIO](https://b1ackhand.tistory.com/268)
- [자바의 이모저모 : static, final, 캐스팅](https://b1ackhand.tistory.com/269)
- [나의 WAS 변경점, 한계 그리고 개선사항](https://b1ackhand.tistory.com/267)