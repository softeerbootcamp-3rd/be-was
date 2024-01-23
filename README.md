# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 전체 구조
![img_1.png](img_1.png)

1. Client가 HttpMessage를 보내면 Webserver에서 생성된 thread를 통해 RequestHandler로 진입 
2. HttpRequestUtils에서 HttpMessage에 대한 parsing을 진행하고 HttpRequest 객체를 만들어서 반환
3. RequestUrl을 통해 ControllerMappingMap에 등록된 Controller를 찾아서 반환
4. HttpRequestUtils에서 반환된 HttpRequest와 RequestHandler에서 자체적으로 생성한 HttpResponse를 Controller에 넘겨주고 컨트롤러 자체 로직을 실행, Controller에서는 클라이언트에 반환할 값이 세팅된 HttpResponse 객체를 반환
5. 반환된 HttpResponse 객체를 HttpResponseUtils에 넘겨주고 해당 클래스에서 render를 통해 Client에게 Http 응답 메시지를 전송

## 클래스 별 역할
### WebServer
- Java ExecutorService
  - 병렬 작업 시 여러 개의 작업을 효율적으로 처리하기 위해 제공되는 JAVA 라이브러리
  - 쓰레드를 직접 생성하고 제거하는 작업이 불필요
  - ExecutorService에 Task만 지정해주면 알아서 쓰레드 풀을 통해 Task 실행, 관리
     ```angular2html
      executorService.submit(new RequestHandler(connection));
     ```
  - Task 관리 방식 : 쓰레드 풀의 쓰레드 수보다 Task가 많으면 Task는 큐에 저장, 이후 순차적 실행
  - FixedThreadPool을 사용하여 톰캣의 최대 쓰레드 풀 사이즈인 200으로 설정

### HttpRequest, HttpResponse
- Http 요청과 응답 객체 
- 필드와 getter, setter로만 구성

### HttpRequestUtils
- HttpRequest Message를 Parsing하여 HttpRequest 객체에 저장하는 역할 수행
- RequestLine, Header, Body Parsing 메소드 단위로 진행

### HttpResponseUtils
- Enum으로 선언된 HttpResponseStatus를 가지고 HttpResonseLine을 반환하는 함수 제공
- OutputStream을 가지고 HttpResponse를 실제 Http 응답으로 보내는 역할 수행

### ControllerMappingMap
- Http 요청의 Method와 URL을 해당 컨트롤러와 매핑

# 공부 내용 정리
## JAVA ExecutorService

### About ExecutorService

- 병렬 작업 시 여러 개의 작업을 효율적으로 처리하기 위해 제공되는 JAVA 라이브러리
- Thread를 직접 생성하고 제거하는 작업이 불필요
- Executor Service에 Task를 지정해주면 쓰레드풀을 통해 알아서 실행하고 관리
- Task의 관리 방식
    - Queue
    - 쓰레드 풀의 쓰레드 수보다 Task가 많으면 큐에 저장되었다가 쓰레드가 할당되면 순차적 수행

### 사용

1. ThreadPoolExecutor로 객체 생성 

```java
ExecutorService executorService = new ThreadPoolExecutor(...);
```

1. Executor 클래스에서 제공하는 Static Factory Method 사용

```java
ExecutorService executorService = Executors.newFixedThreadPool(int nThreads);
```

- CachedThreadPool
    - 쓰레드를 캐싱
    - 쓰레드 제한 없이 생성 & 해당 쓰레드 60초간 작업 없으면 Pool 제거
    - 작업이 계속적으로 쌓이는 환경에서 쓰레드 수 폭발적 증가할 가능성
- FixedThreadPool
    - 고정된 개수의 쓰레드 풀
- SingleThreadExecutor
    - 한 개의 쓰레드로 작업 처리
    - race-condition 같은 부분 알아서 처리

### ExecutorService에 Task 할당

- ThreadPool의 쓰레드들이 각자 본인의 Task 수행, 개발자는 쓰레드 생명주기 관리 필요 X
- Method
    - execute()
    - submit()
    - invokeAny()
    - invokeAll()

### 종료

- Method
    - shutdown() : 실행 중인 모든 Task가 수행되면 종료
    - shutdownNow() : 실행 중인 쓰레드들 즉시 종료하려 시도, 모든 쓰레드 동시 종료 보장X, 실행되지 않은 Task 반환

## Http Request Body Parsing

### BufferedReader read vs readLine

- readLine
    - 한 줄씩 읽어옴
    - 각 줄을 문자열로 변환하는 overhead
    - 내부적 버퍼 사용
- read
    - 바이트 단위로 읽어옴
    - 별도의 문자열 변환 필요 X
    - 직접 버퍼 관리가 가능, 성능 최적화

- Http Header처럼 각 헤더가 한줄로 되어 있는 경우 readLine으로 처리하기 좋음. 하지만 Body처럼 큰 데이터의 경우 readLine에서는 한 줄을 읽을 때까지 block되기 때문에 비효율적임.
- 그래도 Http Body가 텍스트 or 멀티파트 형태인 경우 readLine 사용을 고려해볼만 함.

   
