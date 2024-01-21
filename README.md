# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 🐧공부한 것
### 💡소켓 통신
소켓 번호를 통해 byte stream으로 통신

[서버 클래스(`WebServer.java`)에 필요한 것]

> 서버 소켓

클라이언트의 요청을 받기 위한 준비

> 소켓

실제 데이터 주고 받기

</br>

### 💡자바 스레드 모델

자바 프로그램 실행 → JVM 프로세스 실행 → JVM 프로세스 안에서 여러 개의 스레드 실행

- 각 스레드마다 Java Stack, PC register, Native Method stack

>**Why thread?**

- 프로세스 자원 많이 필요(최소 32MB)
- 스레드는 1MB이내

>**Multi Threading Model**

- `User Level Thread`
    - 사용자 라이브러리로 사용자가 만든 스레드
    - 스레드가 생성된 프로세스 자체에 의해 제어됨, 커널은 사용자 스레드에 대해 모름
- `Kernel Level Thread
    - 커널에 의해 생성되고 운영체제가 관리
    - OS 제어
- 스레드를 실행하기 위해서는 CPU 스케줄러(커널)이 CPU 스케줄링 해야함.
    - 근데 앞에서 커널은 사용자 스레드에 대해 알지 못한다고 나와 있음
    - →사용자 스레드는 커널 스레드에 매핑되어 실행됨
- 커널 스레드와 사용자 스레드의 매핑 방법
    - Many-to-One Model
  
      ![Untitled.png](..%2F..%2FDownloads%2FUntitled.png)
        - 한 스레드가 blocking 되면 전체 프로세스가 block
        - 한번에 하나의 스레드만 커널에 접근할 수 있어 멀티 코어 시스템에서는 병렬로 실행될 수 없음(멀티 코어의 이점을 살릴 수 없음)
        - 동기화 및 리소스 공유가 쉬워 실행시간 단축됨
        - 초기 버전 스레드 모델인 Green Thread

    - One-to-One Model

      ![Untitled (1).png](..%2F..%2FDownloads%2FUntitled%20%281%29.png)

        - 하나의 스레드 blocking 되어도 다른 스레드가 실행될 수 있어 병렬 실행이 용이
        - 단점은 사용자 스레드를 위해 커널 스레드를 만들어야 한다는 것
            - 많은 수의 커널 스레드는 시스템 성능에 부담
        - 대부분의 운영체제
    - Many-to-Many Model

      ![Untitled (2).png](..%2F..%2FDownloads%2FUntitled%20%282%29.png)
        - 앞의 두 모델 단점 절충
        - 가장 높은 정확도의 동시성 처리 제공
        - aka. two-level-model
            - 일대일 모델로의 동작도 허용하기 때문
        - 구현이 어려움


자바의 스레드 모델: 다대다 모델(Native Thread)
- by OS
- since java 1.2

### 💡 유틸리티 클래스
인스턴스 메서드와 변수가 없고, 오로지 정적 메서드와 변수만 있는 클래스.
비슷한 기능의 메서드와 상수를 캡슐화 하는 의의

### 💡 HTTP GET Method

server로 부터 resource를 가져온다는 request

> Usage
>
- no body message
- limited to 255 characters
- only support string data types
- GET method will return a response in the message body.(왜냐면 서버에서 가져오겠다는 요청이니까)
    - message body가 없다면(ex: testing the validity) HEAD나 CONNECT method 쓰는 것이 좋다.
    - results are **cacheable**
    - read-only method이기 때문에 safe HTTP method
    - **idempotent**(멱등)
        - 연이어 똑같은 요청들이 들어와도 서버의 상태는 변하지 않음.


> Conditional GET
>

request message body에 If-Match, If-None-Match, If-Modified-Since, If-Unmodified-Since, If-Range같은 validator가 있음.

- 조건이 만족할 때만 데이터 가져옴
- 조건을 잘 만족하면서 성공적으로 자원을 가져오면 200OK
- If-Match나 If-Modified-Since가 실패하면 304 Not Modified 코드와 함께 data가 오지 않음.
    - When the server returns a 304 status, it means that the requested resource hasn't changed since the client's last request that included these validators.
    - 변경되지 않은 데이터를 또 보내면서 bandwidth 소모하지 않음.
- If-None-Match나 If-Unmodified-Since가 실패하면 412 Precondition Failed 코드.
- If-Range가 성공하면 206 Partial Content고, 조건이 완전히 만족되지 않으면 200OK

- 예시

```bash
GET / HTTP/1.1
Host: www.example.re
If-Modified-Since: 01 Jan 2021 12:00:00 GMT
```

> Partial GET
>

Range specifier included

- data의 subset이 return되길 바람.
- 206 Partial Content 성공
- 416 Range Not Satisfiable error 실패
- 근데 서버가 Range header field를 지원하지 않으면 entire document와 함께 200 OK 보낼 수도 있음

- 예시

```bash
GET / HTTP/1.1
Host: www.example.re
Range: bytes=0-511
```

## 💡 단위 테스트 시 객체의 필드값 같은지 확인

assertThat(new UserDTO(~~)).isEqualTo(RequestParser.parseUserInfo())처럼 isEqualTo에 객체를 넣으면 isEqualTo가 두 객체의 참조 동등성을 비교하게 됨. 그래서 fail

**`isEqualTo`** 대신 **`isEqualToComparingFieldByField`** 메서드를 사용하면 됩니다. 이 메서드는 객체의 모든 필드를 비교하여 동등성을 판단

## 💡 클린코드

출처: https://www.nextree.io/basic-of-clean-code/#fn5

> 정의
>
- 단순하여 읽기 쉬움
- 주어진 하나의 일만 담당
- 복잡하지 않고, 모호하지도 않음.

> How
>
- 함수→동사, 클래스나 속성→명사
- 변수는 어떤 것을 위한 값인지 분명히 나타낼 수 있게 이름지어야 한다.
- camelCase
- 하나의 함수가 하나의 일만 수행하자(의미있는 이름으로 함수를 추출할 수 있다면 한 가지의 일만 하고 있는 것이다.)
    - 함수 안의 줄 수는 적을 수록 좋다.
    - 들여쓰기 레벨도 최대 2단까지만 있는 것이 가장 바람직하다
- 함수 인수 최대한 적게 쓰기
    - 가장 좋은 것은 인수가 없는 것, 다음은 단항 함수
    - 입력값이 있는 변환 함수는 반환 값이 있는 것이 좋다.
    - 인수가 많이 필요하다면 독자적인 클래스 생성을 고려하는 것도 좋은 방법이다.
- 명령과 조회 분리하기
- 주석을 최소화하자
    - 잘못된 코드는 빠르게 수정되지만, 잘못된 주석은 잘 수정되지 않는다.
    - 코드 자체가 self-descriptive하게!
- null을 리턴하거나, 인수로 받지 말자.
    - 예외를 던지거나, null에 대응하는 다른 객체 돌려주기

## 🐧 STEP 1 구현 방법
>### 💡 정적인 html 파일 응답
FileReader 유틸클래스에서 Files.readAllBytes를 통해 html 파일 읽어옴.
>### 💡 HTTP Request 출력
RequestParser라는 유틸클래스를 만들어 Request Path, Host, Method, Accept, User-Agent, Cookie 정보 파싱
>### 💡 Concurrent Package 사용
WebServer에서 ExecutorService를 사용하여 스레드 제어

</br>

## 🐧 STEP 2 구현 방법
>### 💡GET으로 회원가입 기능 구현
FileLoader 클래스에서 html 파일 읽어옴. </br>
html, css, js 같은 파일 읽어오는 요청과 유저 생성 요청을 분리함.

>### 💡회원가입 폼에서 `가입` 버튼 클릭하면 사용자가 데이터베이스에 저장됨
userId, password, email, name을 해시맵에 저장함. </br>
이 정보를 꺼내서 Database에 저장

