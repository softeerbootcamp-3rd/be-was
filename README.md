# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

---

<details>

<summary> ✏ STEP 1 학습 기록 </summary>
<div markdown="1">

## 자바 Thread 버전별 차이점

- java 1.0
    - 초기 버전으로, Thread 클래스 도입
    - Thread 클래스는 Runnable 인터페이스를 구현하여 스레드 실행 처리
- java 1.2
    - Thread 우선순위 지정, 스레드 그룹화 및 스레드 상태 확인 기능 추가
- java 5.0
    - java.util.concurrent 패키지가 도입되어 스레드 관리를 더욱 향상
    - Executor 프레임원크와 Callable, Future 인터페이스 등의 기능을 제공
    - 스레드 풀을 구현하기 위한 ExecutorService 인터페이스가 도입
- java 7
    - Fork/join 프레임워크 도입
        - 작업을 작은 하위 작업으로 분할하고 병렬로 실행하는 기능을 제공
    - Fork/join 프레임워크로 인해 Work Stealing Thread Pool이 구현되었음
- java 8
    - 람다식이 도입되어 스레드 코드 작성을 더욱 간결하게 함
    - java.util.concurrent 패키지에 CompletableFuture 클래스가 추가되어 비동기 프로그래밍을 더 쉽게 할 수 있게 되었다.
- java 9
    - Reactive Streams API를 도입하여 비동기 스트림 처리를 지원
    - java.util.concurrent.Flow 패키지가 추가
- java 11
    - java.util.concurrent 패키지에 추가적인 기능 도입 - 스레드 미션, 스레드 로컬 러닝 등이 추가
- java 15
    - java.lang.Thread 클래스에 isInterrupted() 및 interrupt()와 같은 메서드가 추가되어 스레드 인터럽트 상태 처리가 쉬워짐

## java.util.concurrent 패키지

> 다중 스레드 프로그래밍을 지원하는 일련의 클래스 및 인터페이스를 제공한다.

### 주요 인터페이스

[Executor]

- Thread pool 을 구현하기 위한 인터페이스
- 등록된 작업(Runnable)을 실행하기 위한 인터페이스
- 등록된 작업을 실행하는 책임만 가짐 - ISP

[ExecutorService]

- 스레드 사용시 실질적으로 사용하는 인터페이스
- 작업 등록 뿐만 아니라 실행을 위한 책임도 가진다
- Executor 상속받은 인터페이스로, Callable 실행, Executor 종료, 여러 Callable을 동시에 실행하는 기능 제공
    - Runnable과 Callable의 차이는?
        - Runnable은 리턴 값이 없다.
        - Callable은 특정 타입의 객체를 리턴 할 수 있다.


## Excutors의 역할
- Executor, ExecutorService 등은 쓰레드 풀을 위한 인터페이스다. 직접 스레드를 다루는 것은 번거로우므로, 이를 도와주는 팩토리 클래스인 Executors가 등장했다.

> Runnable만 개발자가 만들고 생성, 종료, 없애기 작업들은 Executors가 해준다.

1. Thread 만들기
2. Thread 관리 - 스레드의 생명 주기 관리
3. 작업 처리 및 실행

- newFixedThreadPool
    - 고정된 스레드 개수를 갖는 스레드 풀을 생성
- newCachedThreadPool
    - 필요할 대 필요한 만큼의 스레드 풀을 생성함
    - 이미 생성된 스레드가 있다면 이를 재활용 할 수 있음
- newScheduledThreadPool
    - 일정 시간 뒤 혹은 주기적으로 실행되어야 하는 작업을 위한 스레드 풀을 생성

### 사용방법

```java
1. 구현체 생성
ExecutorService executorService = Executors.newSingleThreadExecutor(); //singleThread
: Executors 클래스의 static Method를 활용하여 ExecutorService 구현체를 SingleThread 형태로 리턴

ExecutorService executorService = Executors.newFixedThreadPool(Thread 개수);//MultiThread
: // 멀티 스레드 형태로 리턴

2. 작업 제출
executorService.sumit();
: Thread에 활용할 작업을 제출(해당 스레드가 대기 중인 경우 제출한 작업을 처리)

3. 작업 종료
executorService.shoutdown();
: 현재 진행중인 작업을 마치고 Thread를 종료한다.

4. 즉시 종료
executorService.shoutdowmNow();
: 현재 진행중인 작업을 마치지 않은 채로 종료할 수 있다.

```
### 추가로 공부할 주제
- Virtual Thread



