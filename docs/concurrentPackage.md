https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html<br>
https://zion830.tistory.com/57

Concurrent 패키지는 다중 스레드 환경에서 작업을 효과적으로 처리하기 위한 클래스와 인터페이스를 제공합니다. 
이 패키지는 Java 5부터 도입되었으며, 스레드 풀, 동시성 컬렉션, 동기화 및 비동기화 작업 등을 지원합니다.

### 주요 요소

#### Executor Framework

- `Executor` : 작업을 비동기적으로 실행하기 위한 메서드를 정의하는 인터페이스입니다.
- `ExecutorService` : `Executor`를 확장한 인터페이스로, 스레드 풀을 관리하고 작업을 제출하는 메서드를 추가로 제공합니다.
- `ScheduledExecutorService`: `ExecutorService`를 확장한 인터페이스로, 예약된 작업을 지원합니다.

#### 스레드 풀 (Thread Pools)

- `ThreadPoolExecutor` : 스레드 풀을 제공하는 클래스로, 스레드 생성, 관리, 풀링 등을 다양한 옵션으로 제어할 수 있습니다.
- `Executors` : 스레드 풀을 생성하기 위한 간단한 팩토리 메서드를 제공하는 유틸리티 클래스입니다.

#### 동시성 컬렉션 (Concurrent Collections)

- `ConcurrentHashMap` : 동시성을 고려한 해시 맵입니다.
- `CopyOnWriteArrayList` 및 `CopyOnWriteArraySet` : 동시성을 고려한 리스트와 집합입니다.
- 기타 동시성을 고려한 컬렉션들도 있습니다.

#### 동기화 및 Locks

- `ReentrantLock`: Reentrant 특성을 가진 락을 제공합니다.
- `ReadWriteLock`: 읽기와 쓰기 작업을 구분하여 동시성을 향상시키는 인터페이스입니다.
s
#### 원자성 (Atomicity)
- `AtomicInteger`, `AtomicLong`, `AtomicReference` : 원자적인 연산을 수행할 수 있는 클래스들을 제공합니다.

#### ForkJoin 프레임워크

- `ForkJoinPool` : 분할 정복 알고리즘을 효율적으로 처리하기 위한 프레임워크를 제공합니다.

#### CompletionService

- `CompletionService` : 작업이 완료된 순서대로 결과를 얻을 수 있는 기능을 제공합니다.

---

### Thread 와의 차이

#### 기본 스레딩 vs 고수준 동시성 유틸리티

- Thread : `Java`의 기본 스레딩 기능을 제공하는 클래스입니다. 
  - Thread 클래스를 직접 확장하거나 `Runnable` 인터페이스를 구현하여 스레드를 생성하고 사용할 수 있습니다.
- Concurrent : 고수준의 동시성 유틸리티를 제공합니다. 
  - 스레드 풀, 동시성 컬렉션, 원자적 연산 등과 같은 다양한 동시성 기능을 제공하여 스레드 프로그래밍을 더 
    효과적으로 다룰 수 있게 도와줍니다.

#### 스레드 

- Thread : 개발자가 직접 스레드를 생성하고 시작해야 합니다.
  - 이로 인해 스레드 생성 및 관리에 대한 부담이 있을 수 있습니다.
- Concurrent : 스레드 풀을 사용하여 스레드를 효율적으로 관리할 수 있습니다.
  - `ExecutorService` 인터페이스를 사용하여 스레드 풀을 생성하고 작업을 제출할 수 있습니다.

#### 동시성 컬렉션

- Thread : 일반적인 컬렉션을 사용할 경우 여러 스레드 간에 동시 접근이 발생할 때 문제가 발생할 수 있습니다.
- Concurrent : `ConcurrentHashMap`, `CopyOnWriteArrayList` 등과 같은 동시성을 고려한 컬렉션을 제공하여 
  여러 스레드 간에 안전하게 사용할 수 있습니다.

#### 원자적 연산

- Thread : 원자적 연산을 보장하는 메서드가 없어 여러 스레드 간의 안전한 데이터 공유에 어려움이 있을 수 있습니다.
- Concurrent : `AtomicInteger`, `AtomicLong`, `AtomicReference` 등과 같은 클래스를 사용하여 원자적 연산을 
  수행할 수 있습니다.

#### 동기화 및 락

- Thread : `synchronized` 키워드 및 `wait()`, `notify()` 메서드 등을 사용하여 동기화를 구현할 수 있습니다.
- Concurrent : `ReentrantLock`, `ReadWriteLock` 등과 같은 고급 락을 사용하여 동기화를 효과적으로 처리할 수 있습니다.

---

### Concurrent 사용시 이점

- 고수준의 추상화
  - `Concurrent` 패키지는 고수준의 추상화를 제공하여 개발자가 더 쉽게 다중 스레드 프로그래밍을 할 수 있도록 도와줍니다.
  - 스레드 생성, 관리 및 동기화에 필요한 복잡한 작업을 추상화하므로 개발자가 높은 수준의 개념으로 프로그래밍할 수 있습니다.


- 스레드 풀 관리
  - `ExecutorService`를 사용하여 스레드 풀을 손쉽게 관리할 수 있습니다.
    - 스레드의 생성, 종료 및 재사용을 자동으로 처리할 수 있습니다.
    - 자원 사용의 효율성과 응답성을 향상시킵니다.


- 동시성 컬렉션
  - `Concurrent` 패키지는 동시성을 고려한 컬렉션을 제공합니다.
    - 여러 스레드가 동시에 안전하게 컬렉션에 접근할 수 있도록 도와줍니다.
  - `ConcurrentHashMap`, `CopyOnWriteArrayList` 등이 여기에 해당합니다.


- 원자적 연산
  - `Atomic` 클래스들을 사용하여 원자적인 연산을 수행할 수 있습니다.
    - 개발자는 명시적인 동기화 없이도 안전하게 공유 변수를 업데이트할 수 있습니다.


- 고급 락과 동기화
  - `ReentrantLock`, `ReadWriteLock` 등을 사용하여 더 세밀한 동기화 제어를 할 수 있습니다.
    - 특정 상황에서 락의 성능을 최적화하고 더 높은 유연성을 제공합니다.


- 포크/조인 프레임워크
  - `Concurrent` 패키지는 포크/조인 프레임워크를 제공합니다.
    - 병렬 처리를 쉽게 구현할 수 있으며, 멀티코어 환경에서 성능을 향상시킬 수 있습니다.


- 작업 완료 및 실행 결과 관리
  - `ExecutorService`의 `submit` 메서드를 사용하여 작업을 제출하면 `Future` 객체를 얻을 수 있습니다.
    - 작업이 완료되었을 때 결과를 얻거나 작업의 상태를 확인할 수 있습니다.


- 스레드 풀의 크기 제어
  - `ExecutorService`를 사용하면 스레드 풀의 크기를 쉽게 조절할 수 있습니다.
  - 동적으로 스레드의 개수를 늘리거나 줄이면서 작업 부하에 효율적으로 대응할 수 있습니다.
