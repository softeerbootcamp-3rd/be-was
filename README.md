# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## Step 1 - index.html 응답
### 학습 목표
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.

- Java 멀티스레드 프로그래밍을 경험한다.

- 유지보수에 좋은 구조에 대해 고민하고 코드를 개선해 본다.

### 사전지식
자바 프로그래밍

### 기능요구사항
#### 정적인 html 파일 응답
- http://localhost:8080/index.html 로 접속했을 때 ```src/main/resources/templates``` 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

#### HTTP Request 내용 출력
- 서버로 들어오는 HTTP Request의 내용을 읽고 적절하게 파싱해서 로거(log.debug)를 이용해 출력한다.

### 프로그래밍 요구사항
#### 개발환경
- JDK Version: 11 또는 17

- IntelliJ를 사용

#### 프로젝트 분석
- 단순히 요구사항 구현이 목표가 아니라 프로젝트의 동작 원리에 대해 파악한다.

#### 구조 변경
- 자바 스레드 모델에 대해 학습한다. 버전별로 어떤 변경점이 있었는지와 향후 지향점에 대해서도 학습해 본다.

- 자바 Concurrent 패키지에 대해 학습한다.

- 기존의 프로젝트는 Java Thread 기반으로 작성되어 있다. 이를 Concurrent 패키지를 사용하도록 변경한다.

#### OOP와 클린 코딩
- 주어진 소스코드를 기반으로 기능요구사항을 만족하는 코드를 작성한다.

- 유지보수에 좋은 구조에 대해 고민하고 코드를 개선해 본다.

- 웹 리소스 파일은 제공된 파일을 수정해서 사용한다. (직접 작성해도 무방하다.)

### 추가 요구 사항
#### 학습 정리
- 학습한 내용을 README.md와 GitHub 위키를 이용해서 기록한다.

### 추가학습거리
#### HTTP Request Header 예
```
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

#### 모든 Request Header 출력하기 힌트
- InputStream => InputStreamReader => BufferedReader

- BufferedReader.readLine() 메소드 활용해 라인별로 http header 읽는다.

- http header 전체를 출력한다.

> 힌트를 통해 어떻게 접근해야 할지 모르겠다면 다음 동영상을 통해 모든 Request Header를 출력하는 과정을 볼 수 있다. 동영상을 통해 힌트를 얻은 후 다음 단계 실습을 진행한다.

[웹 서버 구현 실습 시작하기 힌트 영상](https://www.youtube.com/watch?v=4kb448OJ7Mw)

#### Request Line에서 path 분리하기 힌트
- Header의 첫 번째 라인에서 요청 URL(위 예제의 경우 /index.html)을 추출한다.

  - String[] tokens = line.split(" "); 활용해 문자열을 분리할 수 있다.

- 구현은 별도의 유틸 클래스를 만들고 단위 테스트를 만들어 진행할 수 있다.

#### path에 해당하는 파일 읽어 응답하기 힌트
- 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다.

- 구글에서 “java files readallbytes”로 검색해 파일 데이터를 byte[]로 읽는다.

```
byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
```

### Step-1 추가 학습 내용
- [WAS 동작원리](https://velog.io/@tin9oo/WAS-%EB%8F%99%EC%9E%91%EC%9B%90%EB%A6%AC)
- [HTTP Request & Response](https://velog.io/@tin9oo/HTTP-Request-Response)
- [자바 멀티스레드 프로그래밍](https://velog.io/@tin9oo/%EC%9E%90%EB%B0%94-%EB%A9%80%ED%8B%B0%EC%8A%A4%EB%A0%88%EB%93%9C-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D)
- [자바 Concurrent 패키지](https://velog.io/@tin9oo/%EC%9E%90%EB%B0%94-Concurrent-%ED%8C%A8%ED%82%A4%EC%A7%80)
- [객체지향 프로그래밍(OOP)과 클린 코딩](https://velog.io/@tin9oo/%EA%B0%9D%EC%B2%B4%EC%A7%80%ED%96%A5-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8DOOP%EA%B3%BC-%ED%81%B4%EB%A6%B0-%EC%BD%94%EB%94%A9)
- [좋은 커밋 메시지 작성](https://velog.io/@tin9oo/%EC%A2%8B%EC%9D%80-%EC%BB%A4%EB%B0%8B-%EB%A9%94%EC%8B%9C%EC%A7%80-%EC%9E%91%EC%84%B1)
- [테스트 주도 개발(TDD)](https://velog.io/@tin9oo/%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%A3%BC%EB%8F%84-%EA%B0%9C%EB%B0%9CTDD)

## Step 2 - GET으로 회원가입
### 학습 목표
- HTTP GET 프로토콜을 이해한다.

- HTTP GET에서 parameter를 전달하고 처리하는 방법을 학습한다.

- HTTP 클라이언트에서 전달받은 값을 서버에서 처리하는 방법을 학습한다.

### 사전지식
- Java 프로그래밍

### 기능요구사항
#### GET으로 회원가입 기능 구현
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동, 회원가입 폼을 표시한다.

- 이 폼을 통해서 회원가입을 할 수 있다.

### 프로그래밍 요구사항
- 회원가입 폼에서 ```가입``` 버튼을 클릭하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.

> /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net

- HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.

- 유지보수가 편한 코드가 되도록 코드품질을 개선해 본다.

### 추가 요구 사항
- Junit을 활용한 단위 테스트를 적용해 본다.

### 추가학습거리
#### HTTP Request Header 예
```
GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

#### Request Parameter 추출하기 힌트
- Header의 첫 번째 라인에서 요청 URL을 추출한다.

- 요청 URL에서 접근 경로와 이름=값을 추출해 User 클래스에 담는다.

  - 접근 경로(위 예에서는 /user/create)와 패러미터를 분리하는 방법은 String의 split() 메소드를 활용하면 된다.

  - ? 문자는 정규표현식의 예약어이기 때문에 split() 메소드를 사용할 때 "?"이 아닌 "\\?"를 사용해야 한다.