# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.
***

## ✏️ Study
### [Java Thread](./docs/JavaThread.md)

### [Java IO vs. NIO](./docs/IOandNIO.md)

### [HTTP, WEB](./docs/HTTP.md)

### [OOP](./docs/OOPandCleanCoding.md)

### [TEST 코드 작성 방법](./docs/Test)

***

## Trouble Shooting
🚨 `.css`, `.js` 파일을 제대로 불러오지 못하고 적용이 안되는 문제
> `.html` 파일이 위치한 경로와 `.css`, `.js` 파일이 위치한 경로가 달라서 발생하는 문제였고, 파일을 불러온 후에도 적용이 안되는 것은 content-type의 문제였다. Step3 요구사항에 있는 걸 모르고 열심히 찾았는데.. 그래도 스스로 문제 원인을 파악하고 해결 할 수 있어서 뿌듯했다.

🚨 `BufferedReader` 를 close 하면 Socket 연결이 끊어지는 현상
> BufferedReader를 닫으면 내부의 InputStream도 닫히기 때문에 발생하는 문제 👉
> InputStream이 닫힐 때 BufferedReader도 자동으로 닫히기 때문에 따로 닫아주지 않아도 무방하다.

🚨 `org.mockito:mockito-junit-jupiter` 의존성 적용이 되지 않는 현상
> `testImplementation 'org.mockito:mockito-core:+'` 코드를 삭제하니 적용이 되었다. 더 이상한 건 한 번 적용이 되고 나니 `org.mockito:mockito-core`을 다시 추가하고 로드해도 적용이 된다는 것이다😱 결국 사용하지 않은 라이브러리지만 해결에 시간을 많이 쏟아서 남겨본다.


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

🌟 의존성 주입(DI; Dependency Injection)
> "객체를 필요로하는 클래스 내에서 직접 객체를 생성하는 것은 클래스를 특정 객체에 커밋하는 것이고 이후에 클래스로부터 독립적으로(클래스의 수정 없이) 인스턴스의 생성을 변경하는 것이 불가능하기 때문에 유연하지 못하다. 이는 다른 객체를 필요로하는 경우 클래스를 재사용할 수 없게하며, 실제 객체를 모의 객체로 대체할 수 없기 때문에 클래스를 테스트하기 힘들게한다"
> 
> **의존성 주입**: 객체 간 결합도를 느슨하게 되도록하고 의존관계 역전 원칙과 단일 책임 원칙을 따르도록 클라이언트의 생성에 대한 의존성을 클라이언트의 행위로부터 분리하는 것
> 
> **Spring**: 스프링 프레임워크는 **IoC(Inversion of Control)** 컨테이너에서 Bean 객체의 생성, 연결, 생명주기 관리 등을 담당한다. IoC는 객체의 생성과 생명주기 관리를 개발자가 아닌 프레임워크에 위임하여 개발자는 비즈니스 로직에만 집중할 수 있게 해준다.
> 그리고 클래스 사이의 의존 관계를 스프링 IoC 컨테이너가 자동으로 연결해주는데, 클래스 내부에서 사용할 객체를 직접 생성하는 것이 아니라, 외부(스프링 IoC 컨테이너)에서 생성된 객체를 주입받아 사용하므로 DI에 해당한다.
>
> Spring에서 의존성을 주입하는 3가지 방법
> * 생성자 주입(Constructor Injection): 객체를 생성할 때 생성자의 매개변수로 필요한 객체를 전달
> * 세터 주입(Setter Injection): 객체의 세터 메서드를 통해 필요한 객체를 전달
> * 필드 주입(Field Injection): 객체의 필드(멤버 변수)에 직접 필요한 객체를 주입

> 💡 UserController 클래스 내에서 UserService를 직접 생성하여 static final로 가지고 있었는데, Controller의 로직만 테스트하기 위한 단위 테스트 코드를 작성하면서 UserService Mock 객체를 만들어 주입할 수 없다는 걸 깨달았다.
> 이렇게 실제로 테스트 코드를 작성하면서 문제를 깨닫고, 원인을 파악하고 왜 이러한 패턴이 생겼는지 이해하면서 코드를 리팩토링하는 과정을 반복하니 정말 내 것이 되는 듯한 느낌이 든다.

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

💡 정적 메소드 테스트하기 - 정적 메소드의 장단점
> static 으로 선언한 메서드는 클래스를 메모리에 로드하는 시점에 메서드가 메소드 영역(Method Area)에 로드되므로 인스턴스를 만들지 않아도 된다는 장점을 가져 유틸리티 메서드나, 상태를 가지지 않는 메서드를 구현하는 데 주로 사용된다.
>
> 하지만, 단위 테스트를 작성할 때 테스트하고자 하는 대상이 책임지지 않는 기능에 대해서는 mocking을 통해 제어하는데 static method의 경우 클래스가 로드 될 때 결정되기 때문에 런타임 시에 결정할 수 있도록 제어를 해주어야한다.
> Mockito의 `mockStatic`을 이용하면 static method를 mocking할 수 있지만 복잡하고 런타임 오버헤드를 발생시킬 수 있으므로, 필요한 경우에만 사용해야 한다.
> 
> 나도 `HttpResponseUtil`과 `WebUtil` 두 개의 유틸리티 클래스 안에서 정적 메소드를 여러 개 사용하고 있는데, 정적 메소드가 OOP에 어긋나는 경우가 많고 유지보수를 어렵게 한다는 걸 테스트 코드를 작성하면서 깨닫게 되었다. 이 메소드를 정적 메소드로 만드는 것이 적절한지에 대해서 더 고민해보고 코드를 작성해야겠다.

🌟 NIO 패키지라고 모두 Non-Blocking으로 동작하는 것은 아니다!
> 
> NIO 패키지 대신 IO 패키지를 사용하라는 요구사항을 적용하기 위해 내가 기존에 사용했던 nio 패키지 코드를 살펴 보았다.
> ```java
> import java.nio.file.Files;
> /// 중략 ..
> body = Files.readAllBytes(new File(request.getPath()).toPath());
> ```
> 나는 위와 같은 방식으로 nio 패키지를 사용하였는데, `Files.readAllBytes`은 Blocking 방식으로 작동하는 메서드였다.
> 해당 메서드의 코드는 다음과 같다.
> ```java
>   public static byte[] readAllBytes(Path path) throws IOException {
>       try (SeekableByteChannel sbc = Files.newByteChannel(path);
>            InputStream in = Channels.newInputStream(sbc)) {
>           if (sbc instanceof FileChannelImpl)
>               ((FileChannelImpl) sbc).setUninterruptible();
>           long size = sbc.size();
>           if (size > (long) Integer.MAX_VALUE)
>               throw new OutOfMemoryError("Required array size too large");
>           return read(in, (int)size);
>       }
>   }
>```
> 메서드 내부에서 `Files.newByteChannel()`을 통해 생성되는 `SeekableByteChannel`은 `ReadableByteChannel`와 `WritableByteChannel`을 구현하고 있는데 해당 채널은 모두 blocking 방식으로만 동작하는 Channel이기 때문이다.
> 
> 따라서 nio 패키지라고 모두 non-blocking 방식으로 동작하는 것은 아니며, `Channel`을 사용하는 I/O는 언제나 Non-blocking 방식으로 동작하는 것이 아니라, "**non-blocking 방식도 가능하다**"는 것을 알고 있어야 한다.
