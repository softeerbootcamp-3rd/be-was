# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.


# STEP1 - 학습일지

## 자바 스레드 모델

- 자바 프로그램을 실행하면 JVM 프로세스 실행 → JVM 프로세스 안에서 GC 를 포함한 여러개의 스레드 실행

    ![Untitled.png](..%2F..%2FDownloads%2F287a71f6-350f-4576-a193-e9551a73774f_Export-a0e28066-d998-4fd1-8c5d-096c7d726d55%2FSTEP1%20-%20%ED%95%99%EC%8A%B5%EC%9D%BC%EC%A7%80%2064b2846c37224121b557c5becf040f73%2FUntitled.png)
    - 각 스레드마다 Java Stack, PC Register, Native Method Stack 이 존재
    - 자바는 동일한 메모리를 읽지만 각각 작업을 수행하는 스레드들, 즉 멀티 스레드를 통해 동시에 여러 작업을 수행
    - [JVM 의 메모리 사용 방식](https://kotlinworld.com/3?category=914495)

<aside>
🔥 왜 프로세스를 추가하는 것이 아닌 스레드라는 개념을 추가했을까?

- 프로세스는 많은 시간과 자원이 드는 비싼 친구!
- JVM 은 벤더마다 다르지만 최소 32MB 이상의 메모리가 필요
- 스레드는 1MB 이내의 메모리를 점유하므로 스레드를 경량 프로세스라고 칭함
</aside>

### Multi Threading Model

스레드는 User Level Threads(ULT)와 Kernel Level Threads(KLT) 2가지 유형이 있음

- **ULT :** 사용자 라이브러리를 통해 사용자가 만든 스레드로, 스레드가 생성된 프로세스이 주소 공간에서 해당 프로세스에 의해 실행되고 관리됨
- **KLT :** 커널에 의해 생성되고 OS 에 의해 직접 관리됨. ULT 보다 생성 및 관리 속도가 느림

두 유형의 차이는 누가 스레드를 제어하느냐의 관점임

ULT 는 스레드가 생성된 프로세스 자체에 의해 제어되고, KLT 는 프로세스 내의 사용자 스레드에 대해 알지 못하고 OS 에 의해 직접 관리됨

- ULT 실행 시나리오
    - 스레드를 실행하기 위해서는 커널의 CPU 스케줄러가 스레드를 CPU에 스케쥴링해야함
    - 하지만 CPU 스케쥴러는 커널의 일부이기 때문에 ULT 에 대해 알지 못함
    - 그래서 ULT 는 KLT 에 매핑되어 실행하게됌
- ULT ↔ KLT 매핑 방법
    - **Many to One Model, 다대일 모델**
        
        ![Untitled 1.png](..%2F..%2FDownloads%2F287a71f6-350f-4576-a193-e9551a73774f_Export-a0e28066-d998-4fd1-8c5d-096c7d726d55%2FSTEP1%20-%20%ED%95%99%EC%8A%B5%EC%9D%BC%EC%A7%80%2064b2846c37224121b557c5becf040f73%2FUntitled%201.png)
        - 다대일 모델은 여러 ULT 를 하나의 KLT 에 매핑함. 스레드는 사용자 공간의 스레드 라이브러리에 의해 관리됨
        - 한 스레드가 Blocking 되면 전체 프로세스가 Block됨
        - 한번에 하나의 스레드만 커널에 접근할 수 있기 때문에 멀티 코어 시스템에서 병렬 실행 불가능 But 동기화 및 리소스 공유가 쉬워서 실행시간이 단축됨
        - Java 의 초기 버전 스레드 모델인 Green Thread 가 이 모델을 채택했지만 대부분의 컴퓨팅 시스템이 멀티 코어로 바뀌면서 해당 모델은 사용되지 않음
    - **One to One Model, 일대일 모델**
        ![Untitled 2.png](..%2F..%2FDownloads%2F287a71f6-350f-4576-a193-e9551a73774f_Export-a0e28066-d998-4fd1-8c5d-096c7d726d55%2FSTEP1%20-%20%ED%95%99%EC%8A%B5%EC%9D%BC%EC%A7%80%2064b2846c37224121b557c5becf040f73%2FUntitled%202.png)
        - 일대일 모델은 각 ULT 를 각각의 하나의 KLT 에 매핑
        - 한 스레드가 Blocking 되더라도 다른 스레드가 실행될 수 있기 때문에 병렬 실행에 용이
        - But ULT 를 위해 KLT 를 만들어야하는 큰 단점 존재, 많은 KLT 는 시스템에 부하를 가하기 때문에 OS 는 스레드 수의 증가를 제한함
    - **Many to Many Model, 다대다 모델**
        ![Untitled 3.png](..%2F..%2FDownloads%2F287a71f6-350f-4576-a193-e9551a73774f_Export-a0e28066-d998-4fd1-8c5d-096c7d726d55%2FSTEP1%20-%20%ED%95%99%EC%8A%B5%EC%9D%BC%EC%A7%80%2064b2846c37224121b557c5becf040f73%2FUntitled%203.png)
        - 해당 모델은 여러개의 ULT 보다 작은 혹은 같은 수의 KLT 로 매핑
        - KLT 의 수는 시스템의 코어 수에 따라 다름. 코어의 갯수가 많을 수록 많은 KLT 를 할당받음
        - 이전 두 모델의 단점을 절충한 방법으로 개발자는 필요한 만큼 ULT 를 생성하고 그에 상응하는 KLT 가 병렬로 수행됨. 가장 높은 정확도의 동시성 처리를 제공하는 모델로, 하나의 스레드가 Blocking 되었을 때 커널은 다른 스레드의 수행을 스케쥴링할 수 있음
        - 일대일 모델로도 동작할 수 있음. 매우 좋아보이지만 실제로 구현하기 어렵고 시스템에서 코어 수가 증가하면서 커널 스레드를 제한하는 중요성이 줄어 대부분의 OS 에서 일대일 모델 차용중

### Java Thread Model

초기에는 Green Thread 를 사용하다가 Native Thread 모델로 변경

Java의 스레드 모델은 Native Thread로, Java의 유저 스레드를 만들면 [Java Native Interface(JNI)](https://velog.io/@vrooming13/JNI-JAVA-Native-Interface)를 통해 커널 영역을 호출하여 OS가 커널 스레드를 생성하고 매핑하여 작업을 수행하는 형태

|  | Green Thread | Native Thread |
| --- | --- | --- |
| Multi Threading Model | 다대일 모델 | 다대다 모델 |
| 구현 및 관리 | 애플리케이션 수준에서 구현되고 사용자 공간에서 관리 | OS 수준에서 구현되고 커널 공간에서 관리 |
| 장점 | 동기화 및 자원 공유가 용이하여 실행 시간이 단축됨 | 높은 정확도의 동시성 처리, 멀티코어 시스템의 활용할 수 있음 |
| 단점 | 멀티 코어 시스템의 이점을 살릴 수 없음 | 스레드 동기화 및 자원 공유가 복잡 → 실행 시간 증가 |

### 버전별 변경점

- **Java 1.0 - 1.1**
    - 초기 버전에서는 스레딩에 대한 기본 클래스들이 제공되지 않았음
    - 개발자는 **`Thread`** 클래스를 직접 확장하거나 **`Runnable`** 인터페이스를 구현하여 스레드를 생성
- **Java 1.2 - 1.4 (Java 2)**
    - Java 2에서는 **`java.lang.Thread`** 클래스 및 **`java.lang.Runnable`** 인터페이스가 도입
    - **`Thread`** 클래스의 메서드 중 일부가 synchronized로 선언되어 멀티스레딩에서 더 쉽게 사용
    - **`synchronized`** 키워드를 사용하여 스레드 간 동기화 지원.
- **Java 5 (Java 1.5) - 동시성 API의 도입**
    - **`java.util.concurrent`** 패키지 도입으로 스레드 풀, 락, 동시 컬렉션 등을 포함한 새로운 동시성 API가 도입
    - **`Executor`** 프레임워크, **`Future`** 및 **`Callable`** 인터페이스, **`Lock`** 및 **`ReentrantLock`** 등이 추가
    - **`volatile`** 키워드의 사용이 변경되어 스레드 간 가시성이 향상되었습니다.
- **Java 6**
    - **`java.util.concurrent`** 패키지에 몇 가지 새로운 클래스 및 개선 사항이 추가되었습니다.
    - **`StampedLock`**이 추가되어 낙관적인 읽기와 쓰기 잠금을 지원했습니다.
- **Java 7**
    - **`ForkJoinPool`**이 도입되어 병렬 처리를 지원하는데 사용됩니다.
    - **`ThreadLocalRandom`** 클래스 도입으로 스레드 간 랜덤 넘버 생성이 향상되었습니다.
- **Java 8**
    - 람다 표현식이 도입되어 함수형 프로그래밍이 가능해졌습니다.
    - **`java.util.stream`** 패키지 추가로 병렬 스트림을 통한 간단한 병렬 처리를 제공.
- **Java 9**
    - **`Flow`** API 도입으로 리액티브 프로그래밍 및 비동기 프로그래밍을 위한 스트림 기능이 향상되었습니다.
- **Java 11**
    - **`java.util.concurrent`**에 **`CompletableFuture`**에 새로운 메서드 추가.
    - **`Thread.onSpinWait()`** 메서드 도입.

### 향후 지향점

- **그 이후 눈여겨볼만한 Thread 변경**
    - **`Virtual Thread` : 경량 스레드 모델**
        - 기존 스레드 모델의 문제점
            - 요청량이 급격하게 증가하는 서버 환경에서는 갈수록 더 많은 스레드 수를 요구하게 되었음. 스레드의 사이즈가 프로세스에 비해 작다고 해도, 스레드 1개당 1MB 사이즈라고 가정하면, 4GB 메모리 환경에서도 많아야 4,000개의 스레드를 가질 수 있음. 이처럼 메모리가 제한된 환경에서는 생성할 수 있는 스레드 수에 한계가 있었고, 스레드가 많아지면서 컨텍스트 스위칭 비용도 기하급수적으로 늘어나게됌
        - 더 많은 요청 처리량과 컨텍스트 스위칭 비용을 줄여야 했는데, 이를 위해 나타난 스레드 모델이 경량 스레드 모델인 
        - **Virtual Thread**
      
          [Java의 미래, Virtual Thread | 우아한형제들 기술블로그](https://techblog.woowahan.com/15398/)
    
          [[Java] 기존 자바 스레드 모델의 한계와 자바 21의 가상 스레드(Virtual Thread)의 도입](https://mangkyu.tistory.com/309)


## 자바 Concurrent 패키지 학습 및 구조 변경

### `Java Thread` VS `Concurrent 패키지`

### 기존 Java Thread 운영 방식의 문제점

```java
while ((connection = listenSocket.accept()) != null) {
    Thread thread = new Thread(new RequestHandler(connection));
    thread.start();
}
```

- 다음과 같은 Thread 운영 방식을 서버에 도입할 경우 동시에 여러 사용자가 접속할 때에 스레드를 계속해서 생성 → 자바 스레드 모델의 경우, thread 를 OS의 자원으로 사용하기 때문에 OS 자원이 빨리 소진되어 서버가 다운될 위험이 있음
- 또한 Thread 가 실행한 task 의 결과를 반환하고 싶은 경우, 기존 Java Thread 로는 불가능한 부분이 존재

### 이를 해결하기 위한 [Concurrent 패키지](https://wiki.yowu.dev/ko/Knowledge-base/Java/java-s-java-util-concurrent-package-for-parallel-programming)

- **`Thread Pool 이란`**
    - 탄생 배경
        - 데이터베이스 및 웹 서버와 같은 서버 프로그램은 여러 클라이언트의 요청을 반복적으로 실행하며 많은 수의 작은 작업들을 처리하는 데 중점을 둠. 서버 응용 프로그램을 이러한 요청을 처리하는 방식은 요청이 도착할 때마다 새 스레드를 만들고 새로 만든 스레드에서 받은 요청을 처리하는 것. BUT 모든 요청에 대해 새 스레드를 생성하는 서버는 실제 요청을 처리하는 것보다 스레드 생성 및 소멸에 더 많은 시간을 소비하고 더 많은 시스템 리소스를 소비. 활성(active) 스레드는 시스템 리소스를 소비하기 때문에 동시에 너무 많은 스레드를 생성하는 JVM은 시스템에 OOM을 유발할 수 있음. 이것은 생성되는 쓰레드의 수를 제한할 필요성을 느끼게함.
    - 운영 방식
        - 쓰레드 풀은 **미리 일정 개수의 쓰레드를 생성하여 관리**하는 기법

        ![Untitled 4.png](..%2F..%2FDownloads%2F287a71f6-350f-4576-a193-e9551a73774f_Export-a0e28066-d998-4fd1-8c5d-096c7d726d55%2FSTEP1%20-%20%ED%95%99%EC%8A%B5%EC%9D%BC%EC%A7%80%2064b2846c37224121b557c5becf040f73%2FUntitled%204.png)
        - 미리 생성된 쓰레드들은 작업을 할당받기 위해 대기 상태에 있게 되는데, 작업이 발생하면 대기 중인 쓰레드 중 하나를 선택하여 작업을 수행. 작업이 완료되면 해당 스레드는 다시 대기 상태로 돌아가고, 새로운 작업을 할당받을 준비.
        - 쓰레드 풀을 사용하면 스레드 생성 및 삭제에 따른 오버헤드를 줄일 수 있으며, 특정 시점에 동시에 처리할 수 있는 작업의 개수를 제한할 수 있음.
- **`How to Use??`**

  [concurrent 패키지 분석](https://engineerinsight.tistory.com/197)

  [스레드풀 활용 예시](https://velog.io/@nnakki/Java스레드풀Thread-Pool)


### 기존 WAS 에 Concurrent 패키지 도입

> **웹 애플리케이션에 쓰레드 풀을 도입하기 좋은 이유 -** 쓰레드풀은 동일하고 서로 독립적인 다수의 작업을 실행 할 때 가장 효과적이다.실행 시간이 오래 걸리는 작업과 금방 끝나는 작업을 섞어서 실행하도록 하면 풀의 크기가 굉장히 크지 않은 한 작업 실행을 방해하는 것과 비슷한 상황이 발생한다. 또한 크기게 제한되어 있는 쓰레드 풀에 다른 작업의 내용에 의존성을 갖고 있는 작업을 등록하면 데드락이 발생할 가능성이 높다. 다행스럽게도 일반적인 네트웍 기반의 서버 어플리케이션 (웹서버,메일서버,파일서버등)은 작업이 서로 동일하면서 독립적이어야 한다는 조건을 대부분 만족한다. - Java concurrency in practice 책 발췌
>
- 변경점

```java
var executor = Executors.newFixedThreadPool(30);
while ((connection = listenSocket.accept()) != null) {
    // STEP-1 [요구 사항 3] Concurrent 패키지를 사용하도록 변경.
    executor.execute(new RequestHandler(connection));
}
```

## 🧨 Thread Pool 사용 시 주의 사항 🧨

1. 다른 작업의 결과를 동시에 기다리는 작업을 대기열에 넣으면 안됨. 이로 인해 위에서 설명한 교착 상태가 발생할 수 있음
2. 스레드 풀은 마지막에 명시적으로 종료시켜야함. 이 작업을 하지 않으면 프로그램이 계속 실행되고 끝나지 않습니다. 풀에서 shutdown()을 호출하여 Excutor를 종료. 종료 후 이 Excutor에 다른 작업을 보내려고 하면 RejectedExecutionException이 발생
3. JVM에서 실행할 수 있는 최대 스레드 수를 제한하여 JVM의 메모리 부족 가능성을 줄이는 것이 좋음.
4. 처리를 위해 새 스레드를 생성하는 반복문을 구현해야 하는 경우 ThreadPool이 최대 제한에 도달한 후 새 스레드를 생성하지 않기 때문에 ThreadPool을 사용하면 더 빠르게 처리하는 데 도움이 됨.