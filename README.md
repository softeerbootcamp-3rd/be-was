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

[출처1](https://letsmakemyselfprogrammer.tistory.com/98)

[출처2](https://e-una.tistory.com/70)

[출처3](https://emong.tistory.com/221)

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

### Java Thread에서 Concurrent 패키지로의 변경

WebServer.java 부분에서 기존의 Thread로 구현된 부분을 다음과 같은 코드로 변경하였다.

`ExecutorService threadPool = Executors.newFixedThreadPool(4);`

`threadPool.submit(new RequestHandler(connection));`

따라서 기존의 Thread 방식에선 호출될 때마다 스레드를 생성하였다면 Concurrent 패키지를 통해 4개의 스레드를 가진 스레드 풀을 지정하여 다 쓰인 스레드를 재사용하도록 하였다.