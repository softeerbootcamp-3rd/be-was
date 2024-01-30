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

``` java
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

</div>
</details>

<details>

<summary> ✏ STEP 2 학습 기록 </summary>
<div markdown="1">

### 테스트 코드란?
-   테스트 코드는 소프트웨어의 기능과 동작을 테스트하는데 사용되는 코드이다.
-   테스트 코드는 개발자가 작성한 코드를 실행하고 예상된 결과가 나오는지 확인하는데 사용된다.

### 테스트 코드를 작성해야 하는 이유

-   코드의 품질 향상
    -   테스트 코드를 통해 발생 가능성 있는 버그를 사전에 찾아내고 방지할 수 있으며, 이는 개발자가 신뢰할 수 있는 코드를 작성할 수 있게 도와준다.
-   문서화
    -   테스트 코드는 개발자가 기능의 동작 방식을 이해하는데 도움이 되는 문서로 작용할 수 있다.
    -   테스트 코드를 통해 코드의 예상 동작을 명확하게 확인할 수 있으며, 개발자 간의 커뮤니케이션 향상에도 도움이 된다.
-   리팩토링
    -   테스트 코드가 있는 경우 코드를 리팩토링할때 기존 기능이 여전히 올바르게 작동하는지 확인할 수 있다.
    -   변경 사항이 예상치 못한 부작용을 일으키지 않도록 하며, 코드의 동작이 바뀌지 않았는지 확인할 수 있다.

테스트 종류
단위 테스트(Unit Test)
> 단위 테스트는 응용 프로그램에서 테스트 가능한 가장 작은 소프트웨어를 실행하여 예상대로 동작하는지 확인하는 테스트이다. 테스트 대상 단위의 크기는 엄격하게 정해져 있지 않지만 일반적으로 클래스 또는 메서드 수준으로 정해진다.

통합 테스트(Integreation Test)
> 통합 테스트는 단위 테스트보다 더 큰 동작을 달성하기 위해 여러 모듈들을  모아 이들이 의도대로 협력하는지 확인하는 테스트이다. 단위 테스트와 달리 개발자가 변경할 수 없는 부분(외부 라이브러리)까지 묶어 검증할 때 사용한다. 이는 DB에 접근하거나 전체 코드와 다양한 환경이 제대로 작동하는지 확인하는데 필요한 모든 작업을 수행할 수 있다.

### JUnit5 란?
> JUnit5 = JUnit Platform + JUnit Jupiter + Junit Vintage
- JUnit5는 자바 프로그래밍 언어를 위한 테스트 프레임워크이다. JUnit은 소프트웨어의 단위 테스트를 작성하고 실행하는 데 사용되는 도구로서, 코드의 품질을 검증하고 버그를 감지하기 위해 개발자들에게 도움을 주는 역할을 한다. JUnit5는 이전 버전인 JUnit4의 다음 세데 버전으로 개발되었다. JUnit5는 자바 8 이상부터 사용 가능하다.

[JUnit 5 User Guide
Although the JUnit Jupiter programming model and extension model do not support JUnit 4 features such as Rules and Runners natively, it is not expected that source code maintainers will need to update all of their existing tests, test extensions, and custo
junit.org](https://junit.org/junit5/docs/current/user-guide/)

### AssertJ
- AssertJ는 자바 프로그래밍 언어를 위한 테스트 단언 라이브러리이다. assertion은 코드의 동작을 검증하고 예상한 결과와 실제 결과가 일치하는지를 확인하는데 사용한다.**
- AssertJ는 테스트 코드를 더 읽기 쉽고 유지보수하기 쉽도록 만들어주는 다양한 메서드와 기능을 제공한다.**

[AssertJ / Fluent assertions for java
AssertJ Fluent assertions for java
joel-costigliola.github.io](http://joel-costigliola.github.io/assertj/)


</div>
</details>

<details>

<summary> ✏ STEP 3 학습 기록 </summary>
<div markdown="1">

### MIME 타입
- Multipurpose Internet Mail Extensions
- 미디어 타입이란 문서, 파일 또는 바이트 집합의 성격과 형식을 나타낸다.
- 간단히 말하면 파일 변환을 의미한다.
- 현재는 웹을 통해 여러 파일을 전달하는데 사용되고 있지만 이 용어가 생길 땐 이메일과 함께 동봉할 파일을 텍스트 문자로 전환해서 이메일 시스템을 통해 전달하기 위해 개발되어 Internet Mail Extensions라고 불리기 시작했다고 한다.

### MIME 사용이유
- 예전에는 텍스트파일을 주고 받는데 ASCII 코드로 공통된 표준에 따르기면 하면 되었으나, 네트워크를 통해 ASCII가 아닌 Binary 파일을 보내는 경우가 생기게 되었다. 음악파일, 비디오파일, 워드파일 등등 ASCII만으로 전송이 불가하기 때문에 기존시스템에서 문제없이 전달하기 위해 텍스트변환이 필요했다.
TCP/IP 네트워크에서 이메일교환 시 표준으로 RFC822(기본 이메일메시지 형식 정의)이메일 메시지 형식이 사용되는데 이 형식은 ASCII코드를 사용하기 때문에 간단한 메시지를 전송할 경우에는 유용하지만, 다른 통신형태까지 모두 지원하기에는 유연성이 부족하다.
따라서, 이러한 다른 통신형태의 다양한 메시지를 지원하기 위해서 MIME표준이 개발된 것이다.

### 웹 개발에 자주 쓰이는 타입 

|type|discription|
|------|---|
|application/octet-stream|바이너리 파일의 기본|
|text/plain|텍스트, unknown textual file이어도 브러우저는 display|
|text/css|웹 페이지 스타일링을 하는 css 파일은 반드시 text/css 타입으로 보내야 함|
|text/html|HTML 파일은 반드시 이 타입으로 보내져야 햠|
|text/javascript|다른 타입으로 보내게 되면 로드나 실행되지 않음|

</div>
</details>

<details>

<summary> ✏ STEP 4 학습 기록 </summary>
<div markdown="1">

### 기능 요구사항
- 로그인을 GET에서 POST로 수정 후 정상 동작하도록 구현한다.
- 가입을 완료하면 /index.html 페이지로 이동한다.

### HTTP redirect
리다이렉트는 HTTP 표준으로 정의 되어 있는데 최초 요청을 받은 웹서버는 HTTP 응답 상태코드로 302를 보내고 응답 메시지 헤더 중 Location 값으로 리다이렉트 되어야 할 주소를 설정해 리턴한다.
클라이언트는 서버로 부터 받은 응답 값이 상태코드 302라는 것을 보고 서버가 리다이렉트를 시킨거구나라고 알고  Location 에 설정되어 있는 URL로 다시 재요청을 한다.

### step-4를 구현하면서 발생한 오류
#### ❗️java.net.SocketException: Broken pipe
원인
- 잦은 입출력 호출로 발생한다.
- 처리중인 요청 또는 응답을 사용자가 기다리지 않고 새로고침 또는 종료를 자주 실행하게 되면 소켓이 끊어져 발생한다.
- 웹 브라우저에서 서버에 연결하면 accept된 소켓을 HttpThread에 전달, ThreadPool에서 조건에 맞으면 해당 HttpThread를 가동하게 되어있다.
- HttpThread가 완료되기 전에 재요청을 할 경우 문제가 생긴다. 처음 요청 때 생성된 소켓의 자원을 httpThread.run()에서 사용하려고 하는 중에 두 번째 요청이 들어와 첫번째 요청의 소켓이 끊어져 버리기 때문이다.

### 문제 상황

body를 추출하는 부분에서 문제를 발견했다.

문제가 발생한 코드

``` java
if(method.equals("POST")){
            String body = br.readLine();
            return new HttpRequest(method, uri, headers, body);
        }
```

문제가 발생한 이유는 body를 POST 요청인 경우 br.readLine()으로 읽어오도록 코드를 작성하였는데,
br.readLine() 메서드의 종료 조건이 ‘\n ‘또는 ‘\r’로 설정되어 있어, 바디를 읽은 후에도 종료 조건이 없어서 계속해서 데이터를 읽어오게 되었다. 이로 인해 회원가입 요청이 계속해서 pending 상태로 남아 있었고, 두 번째 요청 시에는 broken pipe 오류가 발생하는 상황이었다.

### 해결 방법

br.read()를 활용하여 httpRequest의 contentLength 만큼만 읽어오도록 코드를 수정하여 해결할 수 있었다.

해결한 코드

``` java
if(method.equals("POST")){
    int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));
    char[] body = new char[contentLength];
    return new HttpRequest(method, uri, headers, new String(body, 0, br.read(body)));
}
```

</div>
</details>

<details>

<summary> ✏ STEP 5 학습 기록 </summary>
<div markdown="1">

### 쿠키
- key-value 쌍으로 구성되어 있는 데이터 파일
- 쿠키이름, 쿠키값, 만료시간, 전송할 도메인 명, 전송할 경로, 보안연결 여부, HttpOnly 여부로 구성
- 도메인 당 20개의 쿠키를 가질 수 있다.
- 하나의 쿠키는 4KB(=4096byte)까지 저장이 가능

#### 쿠키의 종류
| 쿠키 종류              | 특징                                                          |
|--------------------|-------------------------------------------------------------|
| Session Cookie     | 일반적으로 만료시간을 설정하고 메모리에만 저장되며, 브라우저 종료 시 쿠키를 삭제               |
| Persistent Cookie  | 장기간 유지되는 쿠키, 파일로 저장되어 브라우저 종료와 관계없이 사용할 수 있다.               |
| Secure Cookie      | HTTPS 프로토콜에서만 사용하며, 쿠키 정보가 암호화되어 전송                         |
| Third-party Cookie | 방문한 도메인과 다른 도메인의 쿠키, 일반적으로 광고 배너 등을 관리할 때 유입 경로를 추적하기 위해 사용 |

### 세션
- 일정 시간 동안 같은 사용자로부터 들어오는 일련의 요구를 하나의 상태로 보고, 그 상태를 유치지시키는 기술
- 즉, 브라우저가 종료되기 전까지 클라이언트의 요청을 유지하게 해주는 기술
- 브라우저를 닫거나, 서버에서 세션을 삭제했을 때만 삭제가 되기 때문에 쿠키보다 비교적 보안적으로 우수
- 저장 데이터에 제한이 없다.(서버 용량 허용 범위 내에서)
- 각 클라이언트에 세션 아이디를 부여한다.

### 쿠키와 세션의 차이
|      | 쿠키                               | 세션           |
|------|----------------------------------|--------------|
| 저장위치 | 클라이언트의 웹 브라우저가 지정하는 메모리 또는 하드디스크 | 서버의 메모리      |
| 만료시점 | 쿠키 저장 시 설정                       | 브라우저 종료 시 삭제 |
| 보안   | 비교적 취약                           | 안전           |

- 쿠키는 클라이언트에서 수정할 수 있기 때문에 위변조의 위험이 항상 존재한다.
- 따라서 쿠키값을 암호화해야 하며, 민감하거나 중요한 정보를 담지 않도록 해야한다.

</div>
</details>

