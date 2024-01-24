# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

# 🐧공부한 것
## 💡소켓 통신
소켓 번호를 통해 byte stream으로 통신

[서버 클래스(`WebServer.java`)에 필요한 것]

> 서버 소켓

클라이언트의 요청을 받기 위한 준비

> 소켓

실제 데이터 주고 받기

</br>

## 💡자바 스레드 모델

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

## 💡 MIME 타입

> 인터넷에서 컨텐츠의 타입을 정의하기 위해 사용되는 식별자
>

웹 서버와 클라이언트 간 전송되는 문서의 형식

브라우저가 이 정보를 써서 리소스 표시/처리

> 형식
>

`타입/서브타입`

- **텍스트 문서:**
    - **`text/plain`**: 일반 텍스트 문서
    - **`text/html`**: HTML 문서
- **이미지:**
    - **`image/jpeg`**: JPEG 이미지
    - **`image/png`**: PNG 이미지
    - **`image/gif`**: GIF 이미지
- **오디오/비디오:**
    - **`audio/mpeg`**: MP3 오디오
    - **`video/mp4`**: MP4 비디오
- **응용 프로그램:**
    - **`application/pdf`**: PDF 문서
    - **`application/json`**: JSON 데이터


> 웹 서버
>

Content-Type 필드를 통해 MIME 타입 전송

## 💡 POST

출처 https://http.dev/post

send data to a server and is typically utilized for interacting with a specific resource

- 서버에게 바라는 message body의 표현 방법은 Content-Type에 명시됨.

    ```bash
    application/x-www-form-urlencodedCopied !
    ```

    - 디폴트 content-type
        - key-value pairs(key=value)
        - 각 pair는 &로 구분됨
- POST는 PUT method와 비슷.
    - PUT은 멱등성이 있지만, POST는 아님.
- Uses of POST
    - HTML form으로 받은 데이터 블락 전송
    - 이미 존재하는 자원의 데이터 추가 또는 변경
    - 서버가 클라이언트에게 어떤 정보를 보내고 이를 나중에 다시 가져올 수 있도록 하려면, 서버는 200 OK 상태 코드와 함께 Content-Location 헤더를 사용하여 클라이언트에게 해당 정보가 위치한 곳을 알려줌. 이를 통해 클라이언트는 필요할 때 해당 위치로 가서 정보를 가져올 수 있습니다.
    - 서버에게 어떤 task를 시작하거나 멈추게 명령
    - 새로운 리소스 생성. → 201 created status code

## 💡 쿠키

- 서버가 사용자의 웹 브라우저에 전송하는 작은 데이터 조각
- 브라우저는 이 데이터 조각을 저장해 놓았다가, **동일한 서버에 재요청시** 이 조각을 함께 **전송**
- 쿠키는 **두 요청이 동일 브라우저**에서 들어왔는지 아닌지 판단할 때 사용

  →사용자의 로그인 `상태 유지` 가능

- `stateless HTTP` 프로토콜에서 상태정보 기억 가능

### 🥠 목적

1. **세션 관리**: 서버에 저장해야 하는 로그인, 장바구니, 게임 스코어 등
2. **개인화**: 사용자 선호, 테마
3. **트래킹**: 사용자 행동 기록하고 분석

**예전**에는 클라이언트에 정보 저장할 때 쿠키 씀.

근데 **지금**은 ***modern storage API***(웹 스토리지 API(localStorage, sessionStorage), IndexedDB)를 사용해 정보 저장하는 것을 권장

### 🥠 쿠키를 어떻게 만드나요

**서버**: 응답에 `Set-Cookie` 헤더

```bash
HTTP/1.0 200 OK
Content-type: text/html
Set-Cookie: yummy_cookie=choco
Set-Cookie: tasty_cookie=strawberry

[page content]
```

**클라이언트**: 요청에 `Cookie` 헤더

```bash
GET /sample_page.html HTTP/1.1
Host: www.example.org
Cookie: yummy_cookie=choco; tasty_cookie=strawberry
```

### 🥠 쿠키의 라이프타임

- `세션 쿠키`: 현재 세션이 끝날 때 삭제됨.
- `영속적인 쿠키`: *Expires* 속성에 명시된 날짜에 삭제됨 or *Max-Age* 속성에 명시된 기간 이후에 삭제됨.

```bash
Set-Cookie: id=a3fWa; Expires=Wed, 21 Oct 2015 07:28:00 GMT;
```

### 🥠 Secure 쿠키와 HttpOnly 쿠키

- **`Secure 쿠키`**: *HTTPS* 프로토콜 상에서 암호화된 요청의 경우
    - 그래도 민감한 정보는 쿠키에 저장하면 안 돼
- **`HttpOnly 쿠키`**: XSS 공격을 방지하기 위해 HttpOnly 쿠키는 JS의 Document.cookie API에 접근 못함.
    - 세션 쿠키는 JS에 접근할 필요가 없어 HttpOnly 플래그 설정

### 🥠 쿠키의 스코프

어떤 URL을 쿠키가 보내야 하는지

**`Domain`**: 쿠키가 전송되게 될 호스트들 명시(어떤 웹 사이트 주소로 보낼 건지 지정)

- 명시되지 않으면 현재 문서 위치의 호스트 일부를 기본값으로 함.→현재 웹 문서의 위치에 따라 기본 도메인이 자동 설정됨
- 도메인이 명시되면, 서브도메인들은 항상 포함됨
- 만약 `Domain=mozila.org`로 설정되면, 쿠키들은 `developer.mozila.org`에서도 이 쿠키를 사용할 수 있다.

`Path`: `Cookie` 헤더를 전송하기 위해 요청되는 URL 내에 반드시 존재해야 하는 URL 경로

- Path=/docs로 설정되면, 아래 경로들은 모두 매치
    - /docs
    - /docs/Web
    - /docs/Web/HTTP


### 🥠 Document.cookie를 이용한 JS 접근

HttpOnly 플래그가 설정되지 않았다면 JS로부터 잘 접근될 수 있음

## 🥯 세션

클라이언트와 서버간 저장하고 싶은 데이터를 **서버에 저장**한 후 이 값에 대한 유일한 값(Session Id)발급

- 쿠키를 통해 Session Id만 주고 받음
- 서버는 클라이언트가 전송한 Session Id를 활용해 서버에 저장된 값을 꺼내와 사용함.

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

</br>

## 🐧 STEP 3 구현 방법
>### 💡다양한 컨텐츠 타입 지원
HttpRequest 클래스에서 해당 요청이 어떤 타입의 응답을 요청하는지 알려주는 get함수
- path에서 확장자를 extract </br>

FileLoader에서 HttpRequest에서 가져온 확장자에 따라 중간경로를 설정 후 파일 로드
- 각 확장자의 중간 경로를 해시맵에 저장해놓음

HttpResponseSender에서 MIME-Type에 맞게 헤더의 Content-Type을 작성하여 응답
- 각 MIME type의 Content type 또한 해시맵에 저장해놓음

## 🐧 STEP 4 구현 방법
>### 💡 POST로 회원가입
RequestHandler에서 요청 메소드로 분리(POST, GET)
HttpRequestParser에서 요청 메시지 헤더와 바디 저장
- 바디 저장시 Content-Length 값만큼만 읽어서 무한으로 읽어오는 것 방지

## 🐧 STEP 5 구현 방법
>### 💡쿠키와 세션을 이용한 로그인
로그인이 성공할 경우 HTTP 헤더의 쿠키 값을 SID=세션 ID 로 응답한다.
- HttpResponseSender에서 로그인 성공 후 /index.html로 리다이렉션할 때 `Set-Cookie` 헤더를 통해 세션(id, expireTime) 전달 


세션 ID는 적당한 크기의 무작위 숫자 또는 문자열을 사용한다.
- userService의 login 메서드에서 java.util 패키지의 UUID를 이용해 무작위 숫자 배정