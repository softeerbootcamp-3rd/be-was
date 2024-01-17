## 1. 학습 목표

- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수에 좋은 구조에 대해 고민하고 코드를 개선해 본다.

---

## 2. 요구사항

- 정적인 html 파일 응답
- HTTP Request 내용 출력

---

## 3. 학습 내용

### 1. HTTP Request 구조 분석

- Start Line
  > HTTP 메서드, 요청 타겟, HTTP 버전으로 구성되어 있습니다.
  > - HTTP 메서드 : 서버가 수행해야 할 동작을 나타냅니다.
  > - 요청 타겟 : 주로 URL, 또는 프로토콜, 포트, 도메인의 절대 경로로 나타낼 수도 있으며, 요청 컨텍스트에 의해 특정지어 집니다. (요청 타겟 포맷은 HTTP
      메소드에 따라 달라집니다.)
  > - HTTP 버전 : 응답 메시지에서 써야 할 HTTP 버전을 알려주는 역할을 합니다.

- HTTP Headers
  > 부가적인 정보를 나타내며, 크게 Request 헤더, General 헤더, Representation 헤더로 나눌 수 있습니다.
  > - Request 헤더 : 요청의 내용을 구체화시키고, 컨텍스를 제공하기도 하며, 조건에 따른 제약 사항을 줌으로써 요청 내용을 수정합니다.
  > - General 헤더 : 메시지 전체에 적용되는 헤더입니다.
  > - Representation 헤더 : 메시지 데이터의 원래 형식과 적용된 인코딩을 설명합니다.

- Body
  > 본문은 요청의 마지막 부분에 들어가며, 모든 요청에 본문이 들어가지는 않습니다.
  > <br>리소스를 가져오는 요청은 보통 본문이 필요가 없습니다. 일부 요청은 업데이트를 하기 위해 서버에 데이터를 전송합니다.

### 2. HTTP Response 구조 분석

- Start Line
  > '상태 줄(status line)' 이라고 불리며, 다음과 같은 정보를 가지고 있습니다.
  > - HTTP 버전 : 보통 HTTP/1.1 입니다.
  > - 상태 코드 : 요청의 성공 여부를 나타냅니다.
  > - 상태 텍스트 : 상태 코드에 대한 짧은 설명문입니다.

- HTTP Headers
  > 부가적인 정보를 나타내며, 상태 줄에 포함되지 않은 서버에 대한 추가 정보를 제공합니다.
  > - Response 헤더 : 상태 줄에 포함되지 않은 서버에 대한 추가 정보를 제공합니다.
  > - General 헤더 : 메시지 전체에 적용되는 헤더입니다.
  > - Representation 헤더 : 메시지 데이터의 원래 형식과 적용된 인코딩을 설명합니다.

- Body
  > 본문은 응답의 마지막 부분에 들어가며, 모든 응답에 본문이 들어가지는 않습니다.

### 3. Thread

- Concurrent 패키지의 ExecutorService를 사용하여 Thread를 관리합니다.
  > - ExecutorService는 Thread를 생성하고 관리하는 클래스이며, Thread를 직접 생성하고 관리하는 것보다 편리합니다.
  > - 스레드 풀은 내부적으로 스레드를 관리하여 병렬 처리를 지원합니다.
  > - 고정된 크기의 스레드 풀을 사용하면 스레드를 재사용할 수 있어 스레드 생성 및 소멸에 따른 오버헤드를 줄일 수 있습니다.

---

## 4. 문제 해결

- Content-Type 설정
  > css, js, ico, 등 html이 아닌 다른 파일 형식을 `Content-Type: text/html`으로 지정할 경우
  > 파일이 정상적으로 불러올 수 없는 문제가 있었습니다.

  > 파일 확장자명에 따라 MIME Type을 Content-Type으로 설정하도록 해서 해결했습니다.

---

## 5. 기록

[Request Headers](http/request-header)

[MIME Type](http/MIME-type.md)

[Java Thread Model](thread/java-thread-model)

[OOP](programming/oop.md)

---

## 6. 참고 문서

[MDN - HTTP Messages](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages)

[Oracle - java.util.concurrent](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html)
