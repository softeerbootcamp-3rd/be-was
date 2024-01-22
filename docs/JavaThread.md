# Thread
## Thread
### Thread
* A **thread** (or **lightweight process**) is a basic unit of CPU utilization
* 하나의 프로세스를 구성하는 thread들은 주소 공간을 공유하기 때문에 동일한 코드를 수행하지만, PC를 별도로 두기 때문에 특정한 시각에는 코드의 다른 위치를 수행할 수 있다.
* Implementation of Threads
    * **Kernel Thread**: supported by kernel (OS)
    * **User Thread**: supported by library

### Multi-Thread의 장단점
* Pros
    * Process Context Switching 보다 적은 오버헤드
    * 자원을 보다 효율적으로 사용 가능, 사용자에 대한 응답성(Responsiveness) 향상
* Cons
    * 동기화(Synchronization)에 주의해야 함
    * 교착상태(Deadlock)가 발생하지 않도록 주의
    * 쓰레드 스케줄링 필요

## JAVA Thread Model

### Native Thread (many-to-many)
![Native-Thread](https://techblog.woowahan.com/wp-content/uploads/2023/12/3.png)

* 기존 JAVA의 Thread Model: OS의 도움을 받아 **JVM**이 스레드를 관리하는 모델 (many-to-many 모델)
* JAVA의 유저 스레드를 만들면 **Java Native Interface(JNI)**를 통해 커널 영역을 호출하여 **OS가 커널 스레드를 생성하고 매핑**하여 작업을 수행
* **OS의 도움을 받아 JVM이 스레드를 관리하는 모델**
* 커널은 현재 활동 중인 thread만 관리해야 한다.

👉 서버의 요청량이 급격하게 증가하며 스레드가 많아졌고, 함께 증가하는 Context Switching 비용을 줄이기 위해 경량 스레드 모델인 Virtual Thread 등장

#### Native Thread의 장단점
* Native Thread 모델 이전에 JVM이 사용하던 Green Thread Model (many-to-one) 은 OS 지원 없이 완벽하게 JVM이 user thread를 관리하는 방식으로 멀티 코어(CPU) 환경에서 동작하더라도 그 장점을 전혀 이용할 수 없고 한 번에 하나의 thread만 kernel에 접근하여 처리될 수 있다. 따라서 user thread 중 하나만 block되어도 다른 모든 user thread가 block되는 현상이 발생한다.
* Native Thread는 many-to-many 모델로 멀티 코어(CPU)의 이점을 활용할 수 있으므로 Green Thread에서 하나의 user thread가 block되면 다른 모든 user thread도 block되는 병목 현상을 해결할 수 있고, 한 번에 여러 user thread가 요청을 처리할 수 있다
* 하지만 Native Thread는 커널 스레드에 매핑되기 때문에 스레드를 생성하고 중지하는 데 비용이 많이 든다는 단점이 있다.

### Virtual Thread
![Virtual-Thread](https://techblog.woowahan.com/wp-content/uploads/2023/12/5.png)

* 플랫폼 스레드와 가상 스레드로 나뉘어 플랫폼 스레드 위에서 여러 Virtual Thread가 번갈아 실행
* Context Switching 비용 저렴
    * JVM에 의해 생성되기 때문에 시스템 콜과 같은 커널 영역의 호출이 적고, 메모리 크기도 일반 스레드의 1%에 불과

### JAVA에서 Thread의 구현과 실행
#### 1. `Thread` 클래스 상속
   ```java
    class MyThread1 extends Thread {
        // Thread 클래스의 run() 메소드 오버라이딩
        public void run() {/*...*/}
    }
   ```
#### 2. `Runnable` 인터페이스 구현
   ```java
    class MyThread2 implements Runnable {
        // Runnable 인터페이스의 추상메서드 run() 구현
        public void run() {}
    }
   ```
* `Runnable`은 익명 객체 및 람다로 사용할 수 있지만, `Thread`는 별도의 클래스를 만들어야 함
* Java는 다중 상속이 불가능하므로 `Thread` 클래스를 상속받으면 다른 클래스를 상속받을 수 없음
* Thread 클래스를 상속받으면 Thread 클래스에 구현된 코드들에 의해 더 많은 자원(메모리와 시간 등)을 필요로 함

> 👉 `Thread` 관련 기능의 확장이 필요해서 `Thread` 클래스를 상속받아서 구현해야 할 필요가 있는 경우를 제외하고는 대부분의 경우에 `Runnable` 인터페이스를 주로 사용
>
> 🚨️`Thread`와 `Runnable`을 직접 사용하는 방식은 지나치게 저수준의 API에 의존하고 쓰레드를 직접 관리하는 것이 어려우며, 값의 반환이 불가능하다는 단점 존재

### `Collable`과 `Future` 인터페이스
* `Collable` 인터페이스는 제네릭을 사용해 결과를 받을 수 있음
* 미래에 완료된 `Collable`의 반환값을 구하기 위해 `Future` 인터페이스 사용
    * 비동기 작업의 현재 상태를 확인하고, 기다리며, 결과를 얻는 방법 등 제공
```java
@Test
void future() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Callable<String> callable = new Callable<String>() {
        @Override
        public String call() throws InterruptedException {
            Thread.sleep(3000L);
            return "Thread: " + Thread.currentThread().getName();
        }
    };


    // It takes 3 seconds by blocking(블로킹에 의해 3초 걸림)
    Future<String> future = executorService.submit(callable);

    System.out.println(future.get());

    executorService.shutdown();
}
```
*  3초가 걸리는 작업의 결과를 얻기 위해 `get` 호출 -> `get`은 결과를 기다리는 블로킹 요청이므로 아래의 실행은 적어도 3초가 걸리며, 작업이 끝나고 `isDone`이 `true`가 되면 실행 종료

> Future는 결과를 얻으려면 블로킹 방식으로 대기를 해야 한다는 단점 존재
>
> Future에 처리 결과에 대한 콜백을 정의해 문제를 해결할 수 있는데, 이를 보완하여 Java8에 추가된 것 -> `CompletableFuture`

## JAVA Concurrent 패키지
### 등장 이유
* 동시성 프로그래밍(병렬 프로그래밍)을 지원하기 위한 패키지로 멀티스레드 환경에서 작업을 동기화하고 조율하는 데 사용되며, 스레드 간의 안전한 데이터 공유와 병렬 처리를 위한 다양한 도구와 구조를 제공

### Executor Framework
#### `Executor` 인터페이스
* Thread Pool의 구현을 위한 인터페이스
    * Thread Pool: 매번 새로운 쓰레드를 만드는 대신 쓰레드를 미리 만들어두고 재사용하기 위한 풀
* 등록된 작업(Runnable)을 실행하기 위한 인터페이스
* 작업 등록과 작업 실행 중에서 작업 실행만을 책임짐

```java
@Test
void executorRun() {
    final Runnable runnable = () -> System.out.println("Thread: " + Thread.currentThread().getName());

    Executor executor = new StartExecutor();
    executor.execute(runnable);
}

static class StartExecutor implements Executor {

    @Override
    public void execute(final Runnable command) {
        new Thread(command).start();
    }
}
```

### `ExecutorService` 인터페이스
* `ExecutorService`는 `Executor`를 상속받아서 작업 등록 뿐만 아니라 실행을 위한 책임도 갖는다
* 쓰레드 풀은 기본적으로 `ExecutorService` 인터페이스를 구현
* 라이프사이클 관리를 위한 기능
    * `shutdown`, `shutdownNow`, `awaitTermination`
    * 작업이 완료되었다면 반드시 `shutdown`을 명시적으로 호출해주어야 함
* 비동기 작업을 위한 기능
    * `submit`: 실행할 작업들을 추가하고, 작업의 상태와 결과를 포함하는 `Future`를 반환
    * `invokeAll`: 모든 결과가 나올 때 까지 대기하는 블로킹 방식의 요청, 최대 쓰레드 풀의 크기만큼 작업을 동시에 실행

### `Executors` 팩토리 클래스
* 고수준(High-Level)의 동시성 프로그래밍 모델로써 `Executor`, `ExecutorService` 또는 `SchedueledExecutorService`를 구현한 쓰레드 풀을 손쉽게 생성해준다
* `newFixedThreadPool`: 고정된 쓰레드 개수를 갖는 쓰레드 풀 생성, `ExecutorService` 인터페이스를 구현한 `ThreadPoolExecutor` 객체가 생성됨
* `newCachedThredPool`: 필요할 때 필요한 만큼의 쓰레드 풀 생성, 이미 생성된 쓰레드 재활용 가능
* `newScheduledThreadPool`: 일정 시간 뒤 혹은 주기적으로 실행되어야 하는 작업을 위한 쓰레드 풀 생성, `ScheduledExecutorService` 인터페이스를 구현한 `ScheduledThreadPoolExecutor` 객체가 생성됨
* `newSingleThreadExecutor`, `newSingleThreadScheduledExecutor`: 1개의 쓰레드만을 갖는 쓰레드 풀 생성

***
## Reference
- [Java의 미래, Virtual Thread - 김태헌](https://techblog.woowahan.com/15398/)

- [[Java] Thread와 Runnable에 대한 이해 및 사용법 출처](https://mangkyu.tistory.com/258)

- [[Java] Callable, Future 및 Executors, Executor, ExecutorService, ScheduledExecutorService에 대한 이해 및 사용법](https://mangkyu.tistory.com/259)

- [[Java] Java 에서의 Thread, Light Weight Process](https://blogshine.tistory.com/338)
