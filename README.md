# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.


# 학습 내용 step 1

## 자바 스레드 생명주기

Java에서 스레드의 생명주기는 스레드가 생성부터 종료될 때까지 거치는 여러 상태로 구성된다.

- **New:** 스레드가 생성되면 "New" 상태가 된다. 이 상태에서는 스레드가 인스턴스화되었지만 아직 실행이 시작되지 않았다.
    
    ```java
    Thread newThread = new Thread();
    ```
    
- **Runnable:** 스레드는 실행할 준비가 되었지만 아직 OS에서 실행하도록 스케쥴되지 않은 경우 "Runnable" 상태로 들어간다. 이는 스레드에서 start() 메서드가 호출되었거나 스레드가 스레드 풀에 추가되었음을 의미한다.
    
    ```java
    Thread myThread = new Thread();
    myThread.start(); // Transition to the Runnable state
    ```
    
- **Running:** “Running" 상태에서는 스레드의 코드가 실행된다. OS가 스레드를 스케쥴하면 run() 메서드나 스레드에 제공된 코드 실행이 시작된다.
- **Blocked/Waiting:** 스레드는 I/O 작업, 동기화 잠금을 기다리는 경우 또는 다른 스레드가 이를 알리는 경우 등 다양한 이유로 “Blocking” 또는 “Waiting” 상태에 들어갈 수 있다. 이 상태의 스레드는 코드를 실행하지 않는다.
- **Timed Waiting:** 이 상태의 스레드는 "Waiting" 상태와 유사하지만 시간 제한이 있다.
- **Terminated/Dead:** 스레드는 실행이 완료되거나 명시적으로 중지되면 "Terminated" 또는 "Dead" 상태가 된다. 스레드가 종료되면 다시 시작할 수 없다. Thread.isAlive() 메서드를 사용하면 스레드가 종료된 상태인지 확인할 수 있다.

## 자바 스레드 모델의 변화

### 사용자 수준 스레드와 커널 수준 스레드

스레드에는 사용자 수준 스레드(User Level Thread)와 커널 수준 스레드(Kernel Level Thread)가 있다.

**사용자 수준 스레드**는 사용자 라이브러리를 통해 사용자가 만든 스레드로, 스레드가 생성된 프로세스의 주소 공간에서 해당 프로세스에 의해 실행되고 관리된다. 반면에 커널은 프로세스 내의 사용자 스레드에 대해 알지 못한다.

**커널 수준 스레드**는 커널에 의해 생성되고 운영체제에 의해 직접 관리한다. 사용자 수준 스레드보다 생성 및 관리 속도가 느리고 OS에서 의해 직접 관리된다.

### Green thread model 과 Native thread model

**Green thread**는 JVM에서 생성, 예약 및 관리되는 스레드이며 사용자 공간에서 작동하는 사용자 수준 스레드이다. OS의 지원 없이 JVM 단독으로 생성 및 관리되며 멀티 코어 시스템에서 병렬로 실행 될 수 없다. 한 번에 하나의 스레드 처리로 인해 스레드 동기화를 하기 쉽고 리소스 공유도 간단하다. 한 번에 하나의 스레드가 처리가 돼서 다대일 스레드 모델이라고도 한다.

> 다대일 스레드 모델은 여러 사용자 스레드를 하나의 커널 스레드에 매핑한다. 그래서 한 번에 하나의 스레드 만이 커널에 접근할 수 있다.


**Native thread 모델**에서 OS는 OS의 스레드 API 라이브러리를 이용하여 스레드를 생성하고 관리한다. 스레드가 시스템 수준에서 구현되고 커널 공간 내에서 관리되기 때문에 멀티 코어 시스템을 활용할 수 있다. 여러 스레드가 동시에 실행될 수 있으므로 다대다 모델이라고도 한다. 멀티 스레드 실행으로 인해 스레드 동기화 및 리소스 공유가 복잡해진다.

> 다대다 모델은 여러 개의 사용자 스레드를 그보다 작은 수 혹은 같은 수의 커널 스레드로 멀티플렉스한다.

### 자바 스레드 모델 변화와 virtual 스레드

Java의 초기 버전에서는 green thread를 사용했다. 하지만 대부분의 컴퓨터 시스템에서 멀티 코어가 표준이 되면서 단일 코어에서 실행되는 green thread의 성능 문제 때문에 jdk 1.3 이후로 native thread 모델을 사용한다.

Java의 native thread 모델은 Java의 유저 스레드를 만들면 Java Native Interface(JNI)를 통해 커널 영역을 호출하여 OS가 커널 스레드를 생성하고 매핑하여 작업을 수행하는 형태이다. 이런 스레드 모델은 기존 프로세스 모델을 잘게 쪼개 프로세스 내의 공통된 부분은 공유하면서, 작은 여러 실행단위를 번갈아 가면서 수행할 수 있도록 만든다. 스레드는 프로세스의 공통영역을 제외하고 만들어지기 때문에, 프로세스에 비해 크기가 작아서 생성 비용이 적고, 컨텍스트 스위칭 비용이 저렴했기 때문에 주목받아 왔다.

하지만, 요청량이 급격하게 증가하는 서버 환경에서는 갈수록 더 많은 스레드 수를 요구하게 되었다. 메모리가 제한된 환경에서는 생성할 수 있는 스레드 수에 한계가 있었고, 스레드가 많아지면서 컨텍스트 스위칭 비용도 늘어나게 되었다.

이런 한계를 겪던 서버는 더 많은 요청 처리량과 컨텍스트 스위칭 비용을 줄여야 했는데, 이를 위해 나타난 스레드 모델이 JDK 21에 경량 스레드 모델인 Virtual Thread이다.

Virtual Thread는 기존 Java의 스레드 모델과 달리, 플랫폼 스레드와 가상 스레드로 나뉜다. 플랫폼 스레드 위에서 여러 Virtual Thread가 번갈아 가며 실행되는 형태로 동작한다. 마치 커널 스레드와 유저 스레드가 매핑되는 형태랑 비슷하다.

스레드는 기본적으로 최대 2MB의 스택 메모리 사이즈를 가지기 때문에, 컨텍스트 스위칭 시 메모리 이동량이 크다. 또한 생성을 위해선 커널과 통신하여 스케줄링해야 하므로, 시스템 콜을 이용하기 때문에 생성 비용도 적지 않다. 하지만 Virtual Thread는 JVM에 의해 생성되기 때문에 시스템 콜과 같은 커널 영역의 호출이 적고, 메모리 크기가 일반 스레드의 1%에 불과하다. 따라서 기존 스레드에 비해 컨텍스트 스위칭 비용이 적다.

## 자바 Concurrent 패키지

### Java Thread 기반 코드에서 Concurrent 패키지를 사용한 이유

jdk 17 기준으로 Java에서는 사용자 수준 스레드와 커널 수준 스레드가 각각 존재하며, 사용자 수준 스레드를  커널 수준 스레드에 매핑하는 방식으로 동작한다. 커널 수준 스레드는 OS의 지원을 받아야 하므로 스레드를 매번 요청마다 생성하고 종료하는 것은 시스템의 자원이 많이 소모되고, 성능이 저하된다. 그래서 미리 일정 개수의 스레드를 생성하고 관리하는 스레드 풀을 사용하는게 효과적인데 Concurrent 패키지의 ExecutorService 인터페이스와 Executors 유틸리티 클래스가 스레드 풀을 사용할 수 있는 api를 제공한다.

# 학습 내용 step 4

## HTTP request body를 읽어들일 때 br.readLine() 함수가 아닌 br.read() 함수를 사용한 이유

br.readLine() 함수의 Api 문서를 보면 이렇게 쓰여있다.

> Reads a line of text.  A line is considered to be terminated by any one of a line feed ('\n'), a carriage return ('\r'), a carriage return followed immediately by a line feed, or by reaching the end-of-file (EOF).

br.readLine() 함수는 텍스트의 한 line을 읽는데 이 line은 ‘\n’, ‘\r’, ‘\r\n’, 이나 EOF에 도달하여 끝나는 것을 line으로 한다.

하지만 HTTP 프로토콜에서는 일반적으로 EOF를 명시적으로 표시하지 않는다.  HTTP 메시지의 끝은 Content-Length 헤더를 통해 명시된 길이에 따라 결정되거나, Transfer-Encoding 헤더가 chunked로 설정되어 있다면 각 청크의 끝에 해당하는 CRLF가 사용된다. 따라서 EOF는 명시적으로 표시되는 것이 아니라, 프로토콜 및 헤더에 따라 메시지의 끝을 결정된다.

따라서 body를 읽기 위해 br.readLine() 함수를 쓴다면 EOF를 만나지 못해 계속 무한루프를 돈다. 

body를 읽어오기 위해서 Content-Length를 사용해야 한다. br.read(char[] buf) 함수는 buf의 크기 만큼 스트림에서 읽어오는데 buf 배열의 크기를 Content-Length 크기만큼 할당하고 읽어오면 body를 읽어올 수 있다.

# 학습 내용 step 5

## HTTP Response 상태 코드

### 200 OK

HTTP 200 OK 상태 코드는 요청이 성공했음을 나타낸다. **성공의 의미는 HTTP 요청 방법에 따라 달라진다.**

- GET: uri 요청 리소스가 검색되어 응답 메시지 body에서 전송된다.
- POST: 요청 작업의 결과를 설명하는 리소스는 응답 메시지 body에서 전송된다.

### 301 **Moved Permanently**

요청한 리소스의 URL이 영구적으로 변경된다. Location 헤더에 의해 지정된 URL로 리다이렉션된다. 브라우저는 새 URL로 리디렉션되고 검색 엔진은 리소스에 대한 링크를 업데이트한다.

### 302 Found

요청된 리소스가 Location 헤더가 지정한 URL로 일시적으로 이동되었음을 나타낸다. 브라우저는 이 페이지로 리디렉션되지만 검색 엔진은 리소스에 대한 링크를 업데이트하지 않는다.

### 308 Permanent Redirect

리소스가 이제 Location 헤더에 의해 지정된 다른 URI에 영구적으로 위치함을 의미한다. 301 상태 코드와 동일한 의미를 갖지만 HTTP 메서드를 변경하면 안된다. 첫 번째 요청에서 POST가 사용된 경우 두 번째 요청에서 POST가 사용돼야 한다.

### 401 **Unauthorized**

HTTP 표준에는 "unauthorized"라고 명시되어 있지만, 의미론적으로 이 응답은 "unauthenticated”를 의미한다. 즉, 사용자 이름/비밀번호가 일치하는지 묻기 위해 클라이언트(front-end application)가 인증(authentication)되지 않았음을 의미한다. (ex: 토큰 만료)

### **403 Forbidden**

클라이언트는 컨텐츠에 대한 접근 권한이 없다. 즉, “unauthorized”를 의미한다. 서버는 요청한 리소스를 부여하는 것을 거부하고 있다. 401 상태 코드와 달리 클라이언트의 ID는 서버에 알려진다.

## Reference
- [Java의 미래, Virtual Thread - 김태헌](https://techblog.woowahan.com/15398/)
- [Thread, Java thread model에 대한 이해](https://e-una.tistory.com/70)
- [Green Threads - Priyanka Muniraju](https://priyankamuniraju.medium.com/green-threads-a1f86df8e37a)
- [Multi-Threading in Java - Saurav Kumar](https://tipsontech.medium.com/multi-threading-in-java-b33620ce7b0a)
- [HTTP 상태 코드 - developer.mozilla](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200)
- [로그인시 상태 코드관련 의견 - stackoverflow](https://stackoverflow.com/questions/32752578/whats-the-appropriate-http-status-code-to-return-if-a-user-tries-logging-in-wit)
