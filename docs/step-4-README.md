# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was
를 참고하여 작성되었습니다.

## 1. 학습 목표
- HTTP POST의 동작방식을 이해하고 이를 이용해 회원가입을 구현할 수 있다.
- HTTP redirection 기능을 이해하고 회원가입 후 페이지 이동에 적용한다.

---

## 2. 기능 요구사항
1. 로그인을 GET에서 POST로 수정 후 정상 동작하도록 구현한다.
2. 가입을 완료하면 /index.html 페이지로 이동한다. (redirect 구현)
3. java.nio 에서 java.io 로 변환

---

## 3. 학습 내용 👨🏻‍💻

##### 1. HTTP POST
- HTTP POST는 클라이언트가 서버로 데이터를 전송 및 처리하기 위해 사용하는 메서드이다.
  >- GET방식은 URL에 데이터를 붙여서 보내는 반면, POST방식 HTTP Request body에 데이터를 담아서 전송한다.
  >- Body의 길이는 HTTP Request Header의 Content-Length 헤더에 담아서 전송한다.
  >- 동일한 요청을 여러 번 전송하면 서버의 상태가 변할 수 있는 idempotent하지 않다는 특징이 있다.

##### 2. Java reflection
- 컴파일 시점이 아닌 런타임 시점에 동적으로 특정 클래스의 정보를 추출해낼 수 있는 프로그래밍 기법으로, 힙 영역에 로드되어있는 클래스 타입의 객체를 통해 필드/메소드/생성자를 접근 제어자와 상관 없이 사용할 수 있도록 지원하는 API
- reflection을 사용하는 이유
  >- 접근 제어자를 무시하고 사용할 수 있다.
  >- 컴파일 시점에 존재하지 않는 클래스를 런타임에 클래스의 정보를 동적으로 가져와서 사용할 수 있다.
- step-4 구현 시 reflection을 사용한 이유
  >- jsonParser 메소드에서 User 객체를 생성하고 필드에 값을 할당하기 위해 reflection을 사용하였다. <br>
  User 클래스의 생성자가 private이기 때문에 reflection을 사용하여 인스턴스를 생성하였으며, 필드 또한 private이기 때문에 reflection을 사용하여 필드에 값을 할당하였다.
  >- reflection API 중 하나인 isAnnotationPresent 메소드를 사용하여 @Column Annotation이 있는 필드만 json 데이터를 할당하도록 하였다.
  >- 제네릭 타입인 T를 사용하여 컴파일 시점에 알 수 없 클래스 타입을 런타임 시점에 동적으로 지정할 상호작용 할 수 있도록 하였다.
- 자주 사용되는 메소드
  >- getDeclaredFields(): 클래스의 모든 필드를 가져온다.
  >- getDeclaredMethods(): 클래스의 모든 메소드를 가져온다.
  >- getDeclaredConstructors(): 클래스의 모든 생성자를 가져온다.
  >- setAccessible(true): 접근 제어자를 무시하고 private 필드/메소드/생성자에 접근할 수 있도록 한다.
  >- newInstance(): 클래스의 인스턴스를 생성한다.
  >- invoke(): 클래스의 메소드를 호출한다.
  >- isAnnotationPresent(): 클래스의 필드에 특정 어노테이션이 존재하는지 확인한다.



##### 3. nio vs io
- io (input/output)
  >- 스트림을 사용하여 입출력을 처리하며, 블로킹 방식으로 동작한다.
  >- 데이터를 읽기위해 입력 스트림을 생성하고, 데이터를 출력하기 위해 출력 스트림을 생성해야 한다.
  >- Non-Buffered 방식: 1바이트씩 입출력을 처리한다. 이러한 느린 입출력을 보완하기 위해 BufferedInputStream, BufferedOutputStream을 사용한다.
- nio (non-blocking input/output)
  >- nio는 채널을 사용하여 입출력을 처리하며, 블로킹과 논블로킹 방식으로 동작한다.
  >- io는 스트림을 사용하기 때문에 입력과 출력을 위한 스트림을 따로 생성해야 하지만, nio는 채널을 사용하기 때문에 입력과 출력을 위한 채널을 하나만 생성하면 된다.
  >- Buffer를 사용하여 입출력을 처리한다.
- SocketChannel 사용시에 Non-Blocking 모드로 설정하는 방법
``````java
SocketChannel socketChannel = SocketChannel.open();
socketChannel.configureBlocking(false); // Non-blocking 모드로 설정
``````


- 사전 지식: Blocking vs Non-Blocking
  >- Blocking: 작업이 완료될 때까지 스레드가 대기하는 방식이다. <br>
  io에서는 read(), write() 메소드가 호출되면 입력 스트림 또는 출력 스트림이 데이터를 읽거나 쓸 때까지 스레드가 대기한다. <br>
  이로 인해 스레드가 대기하는 동안 다른 작업을 수행할 수 없기 때문에 자원을 효율적으로 사용할 수 없다.
  >- Non-Blocking: 작업이 완료되지 않더라도 스레드가 대기하지 않고 다른 작업을 수행하는 방식이다. <br>
  비동기 IO를 통해 스레드가 대기하지 않고 다른 작업을 수행할 수 있기 때문에 자원을 효율적으로 사용할 수 있다.

---

## 4. Trouble Shooting 🚀
- UserServiceTest 테스트 코드 작성시에 User 객체 생성에 어려움이 있었다.
  >- 원인: User 클래스의 생성자가 private이고 필드값 또한 private이기 때문에 외부에서 객체를 생성할 수 없었다.
  >- 해결: reflection의 getDeclaredConstructor 메소드를 사용하여 생성자를 가져오고, setAccessible(true) 메소드를 사용하여 생성자를 호출하여 User 객체를 생성하였다.
  >- 고민해 볼 점: reflection을 사용하게되면 유연성이 높아지고, 캡슐화의 본래 목적을 해치게 된다. <br>
  미션에서 외부 라이브러리 사용을 금지하고 있기 때문에 reflection을 사용하였지만, 외부 라이브러리를 사용한다면 mockito와 같은 라이브러리를 공부하여 테스트 코드를 작성을 연습 해야겠다.
- redirect
  >- 원인: redirect uri를 "/index.html"로 하드코딩하여 작성했는데, 이렇게 되면 다른 redirect uri를 사용할 수 없다.
  >- 해결: Controller에서 요청에 따라 redirect uri를 다르게 설정하여 HttpReponse 객체에 Location 헤더를 설정하도록 하였다.
  >   - 변경전: ViewResolver에서 redirect uri를 "/index.html"로 하드코딩하여 작성 

- file read
  >- 원인: nio를 io로 변경함에따라 File.readAllBytes() 메소드를 사용할 수 없었다.
  >- 해결: io의 FileInputStream을 사용하여 파일을 읽었다. <br>
    FileInputStream을 사용하면 1바이트씩 읽기 때문에 BufferedInputStream을 사용하여 8KB씩 읽도록 하였다.

---

## 5. 추가로 학습할 내용 👨🏻‍💻
- First Class Collection
  >- HttpRequest, HttpResponse 클래스를 First Class Collection으로 리팩토링을 진행 할 예정이다. <br>
  구조 변경전에 First Class Collection을 사용하는 Why에 대해 학습하고 정리해보고자 한다.
  >- 학습 내용 정리: https://velog.io/@taegon1998/First-Class-Collection
