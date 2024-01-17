# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 학습 내용

### 정적인 html 파일 응답, HTTP Request 내용 출력

- InputStream을 InputStreamReader - BufferedReader를 통해 라인별로 http header를 읽을 수 있음.
- String.split 메소드를 통해 Header의 첫 라인에서 요청 URL을 알 수 있음.
- Files, Path를 통해 지정된 경로를 통해 바이트 형식으로 파일을 읽을 수 있음.

### Java Thread에 대해

[출처1](https://letsmakemyselfprogrammer.tistory.com/98) [출처2](https://e-una.tistory.com/70) [출처3](https://emong.tistory.com/221) [출처4](https://mangkyu.tistory.com/259)

스레드는 OS가 관리하는가, User가 관리하는가에 따라 Kernel-level, User-level로 구분된다.

현재 자바에서는 Native Thread라는 다대다 모델로서 OS수준에서 구현되고 관리된다.

이는 더 정확한 동시성 처리와 멀티 코어 시스템을 활용하는 장점이 있지만 동기화 및 자원 공유가 복잡해서 실행 시간이 증가한다는 단점이 있다.

자바는 Runnable 인터페이스를 통해 문법적으로 스레드를 지원하고 run 메소드를 가진다.

Thread를 이용하게 되면 코드에 중복 부분이 생기고 에러가 나기 쉽다. 이러한 이유로 자바 5에서 Concurrency API가 소개되었다.

이후 자바 8에서 다양한 클래스와 메소드를 이용해서 프로그래머가 스레드를 활용한 병렬성을 다룰 수 있게 되었다.

### Java Concurrent Package에 대해

Concurrent API는 ExecutorService라는 개념으로 Thread를 대체한다.

Executor는 작업(task)를 비동기적으로 실행시킬 수 있고 스레드 풀을 기본적으로 운영한다.

따라서 프로그래머는 스레드를 직접 만들지 않아도 되고, 자연스럽게 임무를 마친 스레드를 재사용하게 된다.

이러한 ExecutorService를 이용해서 프로그래머가 원하는 만큼 병렬 Task를 만들고 실행시킨다.

또한 Callable을 통해 반환 값을 받을 수 있는 스레드를 생성할 수 있다.

Timeout, Future, ScheduledExecutor 등의 개념들도 프로그래머가 더 효과적으로 병렬 Task를 다룰 수 있게 한다.

### 현재 프로젝트에서 Java Thread에서 Concurrent 패키지로의 변경

WebServer.java 부분에서 기존의 Thread로 구현된 부분을 다음과 같은 코드로 변경하였다.

`ExecutorService threadPool = Executors.newFixedThreadPool(4);`

`threadPool.submit(new RequestHandler(connection));`

따라서 기존의 Thread 방식에선 호출될 때마다 스레드를 생성하였다면 Concurrent 패키지를 통해 미리 생성한 4개의 스레드를 가진 스레드 풀을 지정하여 다 쓰인 스레드를 재사용하도록 하였다.

### JUnit 5 단위 테스트 작성

[출처1](https://yozm.wishket.com/magazine/detail/1748/) [출처2](https://mangkyu.tistory.com/144)

JUnit을 통해 단위 테스트를 작성할 수 있다. 위 프로젝트에서는 AssertJ와 함께 사용하였다. 

assertThat, assertThrows 메소드를 활용해 단위 테스트하는 코드를 작성하였다. 단위 테스트를 작성함으로서 각 메소드들이 예상한대로 동작하는지 검증한다.

@Test, @BeforeEach, @Nested 등의 어노테이션 등으로 테스트 클래스의 상세한 동작을 지정할 수 있다.

테스트를 작성하는 것은 테스트 케이스를 코드보다 먼저 작성하고 코드가 이를 통과하는지 반복하여 프로젝트를 진행하는 개발 방법인 TDD와 관련이 있다.

TDD의 장점에는 기능 구현에 집중하고 향후 리팩토링이 지속할 수 있게 한다는 점이 있다.

### 멀티 스레드 환경에서의 싱글톤 패턴

싱글톤 패턴을 구현해서 적용하였는데 공부하다보니 멀티 스레드 환경에서 잘못 적용하면 여러 개의 인스턴스로 초기화될 수 있다는 것을 알았다.

따라서 기존의 코드를 LazyHolder 패턴을 적용하여 클래스 로딩 시점에 한 번만 생성자가 호출되도록 하였다.

```java
public class FileHandler {
    public static FileHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FileHandler INSTANCE = new FileHandler();
    }
}
```

다음과 같은 코드로 변경하여 내부 클래스는 getInstance가 호출되고 나서야 초기화되므로 JVM에게 객체의 초기화를 맡겨 Thread-Safe를 보장한다. 