# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.
***

## ✏️ Study
### [Java Thread](./docs/JavaThread.md)

### [HTTP, WEB](./docs/HTTP.md)

### [OOP와 클린 코딩](./docs/OOPandCleanCoding.md)

***

## Trouble Shooting
🚨 `.css`, `.js` 파일을 제대로 불러오지 못하고 적용이 안되는 문제
> `.html` 파일이 위치한 경로와 `.css`, `.js` 파일이 위치한 경로가 달라서 발생하는 문제였고, 파일을 불러온 후에도 적용이 안되는 것은 content-type의 문제였다. Step3 요구사항에 있는 걸 모르고 열심히 찾았는데.. 그래도 스스로 문제 원인을 파악하고 해결 할 수 있어서 뿌듯했다.

🚨 `BufferedReader` 를 close 하면 Socket 연결이 끊어지는 현상
> BufferedReader를 닫으면 내부의 InputStream도 닫히기 때문에 발생하는 문제 👉
> InputStream이 닫힐 때 BufferedReader도 자동으로 닫히기 때문에 따로 닫아주지 않아도 무방하다.


## 🧐 Lesson Learned
```text
🌟: Lesson Learned
💡: Explorations
```
🌟 `ExecutorService.shutdown()`
> 기존 코드에서는 프로그램이 종료되면 쓰레드 풀도 자동으로 종료되는 줄 알고 명시적으로 종료하는 코드를 작성하지 않았는데, Concurrent 패키지에 대해 스터디를 하며 쓰레드 풀은 데몬 스레드가 아니기 때문에 프로그램 종료 시 자동으로 함께 종료되지 않는다는 걸 알았다!
> `shutdown()`과 `shutdownNow()` 메서드도 내부적으로 다르게 동작하기 때문에 알아두자.

🌟 `try-with-resources`
> Java 7에서 도입된 문법으로 `try()` 안에서 선언된 객체가 AutoCloseable 인터페이스를 구현하고 있다면 try 구문이 종료될 때 객체의 close() 메소드를 호출해 자원을 자동으로 반환해준다.  
> `try-catch-finally` 패턴을 이용해서 리소스를 반납하던 기존의 방식보다 코드가 단순해 가독성이 뛰어나다는 장점이 있다.

🌟 매직 넘버(Magic Number) 대신 **상수**를 사용하기!
> Magic Number, Magic Literal: 소스 코드에서 의미를 가진 숫자나 문자를 그대로 표현한 것
> 👉 해당 숫자나 문자가 무엇을 의미하는지 바로 알 수 없어서 소스 코드의 가독성을 떨어트림
> 
> 매직 넘버를 상수(static final)로 선언하면 값에 이름이 부여되어 의미와 역할을 확실히 전달할 수 있다!

```java
private static final String DEFAULT_CONTENT_LENGTH = "0";

int contentLength = Integer.parseInt(requestHeaders.getOrDefault("Content-Length", DEFAULT_CONTENT_LENGTH));
```

🌟 `flush()` 메소드에 대한 오해..
> `flush()` 메소드는 `Flushable` 인터페이스에 정의된 메소드로 출력 스트림을 강제로 비우고 저장되어 있던 모든 내용을 목적지로 보낸다. (Flushes this stream by writing any buffered output to the underlying stream)
> 
> 예를 들어, 파일 출력 스트림일 경우 버퍼에 저장된 데이터가 파일에 기록되고 버퍼가 비워지는 것이고 현재 코드처럼 소켓 출력 스트림일 경우에는 버퍼에 저장된 데이터가 클라이언트로 전송 되고 버퍼가 비워진다.

> 처음에는 `dos.flush()` 코드를 통해 Response를 전송하면 소켓 연결이 끊어지길래, 응답을 받으면 자동으로 연결을 끊는다고 생각했지만 `try-with-resources` 구문을 통해 try 블록 안의 모든 내용이 실행된 이후에 자동으로 `close()` 메소드를 실행시키기 때문에 연결이 끊기는 것이었다! 
> 테스트 코드를 작성해보지 않았으면 의문을 갖지 않았을 것 같다.. 다시 한 번 느끼는 테스트 코드의 중요성😇

💡 URL Parameter를 파싱하는 작업의 책임은? Controller vs. Service
> 팀원들 사이에서도 각자 구현한 방식이 달라서 정답이 없는 문제일 것 같다. 
> 
> 개인적으로는 Controller는 유저의 요청을 해석해서 적절한 Service 객체의 메서드를 호출하고, Service는 비즈니스 로직을 수행하는 부분인데 유저의 요청을 적절한 형태로 파싱하는 작업은 비즈니스 로직과는 다른 기능이라고 생각했다.
> 따라서 Controller에서 요청 URL의 쿼리 스트링을 Map 형태로 파싱하여 Service로 넘겨주는 방식을 사용하였다.
 

💡 클라이언트에게 Response를 보내는 작업의 책임은? Controller vs. RequestHandler
> 처음에는 Controller에서 DataStreamOutput을 인자로 받아 `dos.flush()` 코드를 실행하도록 작성했는데, 이렇게 작성하니 중복되는 코드가 생기고 테스트 코드를 작성할 때 주요 로직 이외에 응답을 주고 받는 부분까지 검증을 해야 했고 이는 OOP의 SRP를 위반하는 구조라는 생각이 들었다.
> 따라서 RequestHandler 클래스는 말 그대로 요청을 받고, 응답을 보내는 역할(책임)을 갖게 하고, Controller는 요청을 분석해서 요청을 처리하기 위한 적절한 비즈니스 로직 (Service)를 호출하는 역할을 갖고 있도록 분리하여 구조를 설계하였다.

💡 UserDto를 사용해야 하는가?
> UserDto와 Model.User 사이에 중복되는 코드가 많은데도 UserDto를 굳이 사용해야할까? 에 대한 피드백을 받고 고민을 해보았다.
> 
> DTO (Data Transfer Object)
> * 서로 다른 Layer 간에 데이터를 교환할 때 사용
> * 데이터 은닉: 클라이언트가 필요로 하는 데이터만을 제공함으로써 불필요한 정보를 숨기고, 필요한 정보만을 노출
> * 레이어 간 결합도 감소: 비즈니스 로직이나 도메인 로직이 없는 순수한 데이터 객체로써, 서로 다른 레이어 간의 결합도를 낮추어 유지보수와 확장성 향상
> 
> 도메인 모델(Entity) 대신 DTO를 사용하는 이유
> * SoC (Separation of Concept): Entity는 Database Layer로 초기 설계 후 최대한 수정되지 않는 것이 안정적인 모델이고 View에 노출되서는 안되는 민감한 정보를 포함하고 있을 수도 있다. 그에 반해 DTO의 핵심 관심사는 데이터의 전달이다. 따라서 서로 다른 관심사를 갖고 있는 
> * 클라이언트로부터 서버의 호출을 최소화하기 위해: 한 번의 호출로 여러 매개 변수를 일괄 처리해서 서버와의 왕복을 줄이는 것;
> 
> 결론적으로, 내가 이해하기에 DTO를 사용하는 주요 이유는 정보를 은닉하고 필요한 정보만을 전달하기 위해서라고 생각한다. 따라서 클라이언트의 요구에 따라 서버에 전달되는 데이터의 구조가 자주 바뀔 수 있기 때문에 DTO를 사용하는 패턴이 유지보수와 확장성을 위해 많이 사용된다. 
> 
> 하지만, Step-3 까지의 요구사항에서는 User Entity와 User Dto에서 필요한 정보가 동일하기 때문에 미래에 필요할 것 같다는 이유로 중복된 코드를 생성하는 것은 불필요한 레거시를 생성하는 작업이 된다. 따라서 UserDto를 따로 작성하지 않고 User Entity를 그대로 사용하는 방식으로 수정했다.
