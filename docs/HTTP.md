# WEB
## Web Client & Server

### The Web (Internet)

- massive distributed client/server information system
- In order for proper communication to take place between the client and the server, **applications that running concurrently over the Web must agree on a specific application-level protocol** such as HTTP, FTP, SMTP, POP, and etc.

### 브라우저에서 서버에 요청을 보내고 응답을 받아 화면에 출력하는 과정

![](https://firebasestorage.googleapis.com/v0/b/nextstep-real.appspot.com/o/lesson-attachments%2F-KpOwxr2PBZkaRejxU49%2Fclient-server1.PNG?alt=media&token=037675f8-6a53-49a7-be00-07c6d519c880)

- 네트워크와 연결되어 있는 서버는 고유한 IP를 가짐 → 연결할 웹 서버는 도메인이 아니라 IP를 활용해 접근

![](https://firebasestorage.googleapis.com/v0/b/nextstep-real.appspot.com/o/lesson-attachments%2F-KpOwxr2PBZkaRejxU49%2Fclient-server2.PNG?alt=media&token=882a86cb-3af0-4bf7-957b-e915e9bebdc3)

- 브라우저는 연결할 웹 서버의 IP 주소와 PORT Number를 활용해 Connection을 맺는다 (기본 port는 80)
    - 웹 서버에 Request
    - 웹 서버는 HTML을 생성한 후 Response
    - 브라우저는 Response를 받은 후 웹 서버와 연결을 끊음
    - 브라우저는 응답 Header와 HTML을 읽은 후 웹 자원(js, css, image ..)에 대한 재요청

# HTTP (HyperText Transfer Protocol)
![](https://www3.ntu.edu.sg/home/ehchua/programming/webprogramming/images/HTTP_Steps.png)

## HTTP

- **Protocol**: 데이터를 주고받기 위해 클라이언트와 서버 간 약속한 형식
- **HTTP**: 웹 서버와 클라이언트 간에 약속한 형식
    - **pull** protocol: client pulls information from the server
    - **stateless** protocol: current request does not know what has been done in the previous requests
    - HTTP permits negotiating of data type and representation → 서로 다른 시스템 간에도 효율적으로 통신 가능

> Quoting from the RFC2616: "**The Hypertext Transfer Protocol (HTTP)** is an **application-level protocol** for distributed, collaborative, hypermedia information systems. It is a **generic**, **stateless** protocol which can be used for many tasks beyond its use for hypertext, such as name servers and distributed object management systems, through extension of its request methods, error codes and headers."

## URL (Uniform Resource Locator)

- **URI(Uniform Resource Identifier)**는 URL의 상위 개념이지만 혼용해서 사용하고 있고 같은 것으로 이해해도 무방

```text
protocol://hostname:port/path-and-file-name

ex) http://slipp.net/questions/539
```

- **스킴(scheme), Protocol**: 리소스를 획득하기 위한 방법 (`http`)
- **호스트명**: 리소스가 존재하는 컴퓨터 이름, 도메인 또는 IP (`slipp.net`)
- **Port**: TCP port number
    - port number was not specified in the URL, and takes on the **default number**, which is **TCP port 80 for HTTP**
- **경로명**: 호스트로 지정된 컴퓨터에서의 자원의 위치 (`questions/539`)

## HTTP Request

- 브라우저에서 URL을 입력하면 브라우저가 특정 프로토콜에 따라 URL을 Request Message로 translate해서 서버에게 전송

![](https://www3.ntu.edu.sg/home/ehchua/programming/webprogramming/images/HTTP_RequestMessageExample.png)

### HTTP Request Line

```text
request-method-name request-URI HTTP-version

ex) GET /user/create?userId=javajigi&password=password HTTP/1.1
```

- **Path**: `/user/create?userId=javajigi&password=password`
- **Query String**: `userId=javajigi&password=password`
    - URI 뒤에 쿼리 스트링으로 보낼 수 있는 데이터의 크기에는 한계가 있고, 브라우저의 주소 박스에 노출된다는 단점
    - POST 메소드는 Request Message의 body에 정보를 전달하고 데이터의 크기에 한계가 없음

### HTTP Request Headers

```text
request-header-name: request-header-value1, request-header-value2, ...

ex) 
Host: www.xyz.com
Connection: Keep-Alive
Accept: image/gif, image/jpeg, */*
Accept-Language: us-en, fr, cn
```

- Request Header는 `name: value` 쌍으로 이루어짐: multiple value 가능

## HTTP Method
![http-header-functions (1)](https://github.com/Yoon-Suji/be-was/assets/70956926/67f0d190-550b-4af6-b560-22e8c9997fd5)

### GET vs. POST

- `GET:` Select적인 성향, 서버에서 어떤 데이터를 가져와서 보여준다거나 하는 용도이지 서버의 값이나 상태등을 바꾸지 않음(ex. 게시판의 리스트라던지 글보기 기능)
- `POST`: 서버의 값이나 상태를 바꾸기 위해서 사용 (ex. 글쓰기를 하면 글의 내용이 디비에 저장이 되고 수정을 하면 디비값이 수정)

## HTTP Response

### HTTP Request를 받은 서버가 동작하는 방법

- Request를 해석해서 파일 경로에 위치한 파일을 클라이언트에게 리턴
- Request를 해석해서 서버 안에 위치한 프로그램을 실행시켜 나온 output을 클라이언트에게 리턴
- 잘못된 Request의 경우 에러 메세지 리턴

![](https://www3.ntu.edu.sg/home/ehchua/programming/webprogramming/images/HTTP_ResponseMessageExample.png)

### HTTP Response Status Line

```text
HTTP-version status-code reason-phrase

ex) HTTP/1.1 200 OK
```

- `reason-pharase`: gives a short explanation to the status code

### HTTP Response Headers

```text
response-header-name: response-header-value1, response-header-value2, ...

ex)
Content-Type: text/html
Content-Length: 35
Connection: Keep-Alive
Keep-Alive: timeout=15, max=100
```

- Response Headers는 `name: value` 쌍으로 이루어짐: multiple value 가능

### HTTP Status Code

| Code | reason-pharase                    |
|------|---------------------|
| 200  | OK                  |
| 201  | Created             |
| 302  | Found (HTTP 1.0)    |
| 304  | Not Modified        |
| 401  | Unauthorized        |
| 404  | Not Found           |
| 500  | Internel Server Error |
| 503  | Service Unavailable |
- **2xx**: 성공. 클라이언트가 요청한 동작을 수신하여 이해했고 승낙했으며 성공적으로 처리
- **3xx**: 라다이렉션 완료. 클라이언트는 요청을 마치기 위해 추가 동작이 필요함.
- **4xx**: 요청 오류. 클라이언트에 오류가 있음
- **5xx**: 서버 오류. 서버가 유효한 요청을 명백하게 수행하지 못했음

> 💡 Java Backend 웹 프로그래밍에서 forward(200으로 응답)와 redirect(302으로 응답) 방식으로 응답할 때의 차이점은?
>
> - **Forward (200, OK)**: 웹 서버에서 처리되는 내부이동으로 클라이언트는 동작을 감지하지 못함; 원래의 요청 정보가 유지되기 때문에 클라이언트는 동일한 URL 유지; 주로 서버 내부에서 다양한 리소스 간에 데이터를 공유하고자 할 때 사용
> - **Redirect (302 Found)**: 클라이언트에게 새로운 URL로 이동하라는 메세지를 전달하는 방식; 클라이언트는 새로운 URL로 다시 요청을 보내야 함; 주로 서버 외부로의 이동 또는 새로운 리소스로의 이동에 사용


> 💡 웹 브라우저에서 POST 요청을 처리하는 경우: **POST-Redirect-GET 패턴**
> 
> - 웹 애플리케이션에서 중복으로 데이터를 제출하는 문제를 방지하기 위한 디자인 패턴 (사용자가 폼을 제출한 후에 브라우저를 새로 고침하거나 뒤로가기를 눌렀을 때 중복으로 데이터가 제출되는 것을 방지)
> - 클라이언트가 POST 요청을 서버에게 보내고 서버가 해당 요청을 처리한 후에, 중복 제출을 방지하기 위해 클라이언트에게 리다이렉트할 것을 알리는 응답을 보냄: HTTP 1.1 스펙에서는 303 See Other 상태 코드를 권장하지만 현재까지는 대부분 302 Found 코드 사용
> - 클라이언트는 리다이렉트 응답을 받으면, 지정된 URL로 GET 요청을 보냄: 브라우저에서 새로고침하더라도 GET 요청이 발생하므로 중복으로 데이터가 제출되지 않음
>
> 🚨 모든 중복 submit을 완벽하게 막을 수 있는 것은 아님
> - 리다이렉트 응답을 받기 전에 사용자가 브라우저를 닫는 경우
> - 사용자가 여러 탭이나 창을 사용하여 동시에 submit하는 경우
> - 네트워크 지연 문제


## Content Negotiation

- 클라이언트는 추가적인 Request Headers를 통해서 서버에게 처리할 수 있거나 선호하는 컨텐츠 타입을 알릴 수 있음

### Content-Type Negotiation (MIME type)

- 서버는 **MIME Configuration 파일**(`conf\mime.types`)을 이용해서 파일 확장을 특정 media type에 매핑
    - ex) file extensions `.html` → MIME type `text/html` , `.jpg`, `.jpeg` → `image/jpeg`
- 서버에 여러 타입의 파일이 존재하는 경우, 클라이언트의 요청 헤더의 `Accept` 값에 따라 선호하는 타입을 보내준다

## Non-Persistent HTTP vs. Persistent HTTP

### Non-Persistent HTTP

- 서버가 응답을 전송하면 TCP 연결 close → TCP 연결 하나 당 최대 하나의 요청만 서비스함
- 대부분의 HTML 페이지는 추가적인 리소스에 대한 하이퍼링크가 포함되어 있으므로 브라우저는 동일한 서버에 대해 매번 리소스를 요구할 때마다 TCP 연결을 설정해야 함 → 효율적 X
- HTTP/1.0 의 기본 연결 방식
- non-persistent HTTP response time = 2RTT * 객체의 수 + file transmission time

### Persistent HTTP

- 클라이언트는 서버와 협상하여 응답을 전달한 후 연결을 닫지 않도록 서버에 요청하여 동일한 연결을 통해 다른 요청을 보낼 수 있음
- 클라이언트는 응답을 전달받은 후 연결을 닫도록 서버에 요청하기 위해 `Connection: Close` 헤더를 포함해서 요청을 보낼 수 있음
- 많은 작은 인라인 이미지와 기타 관련 데이터가 있는 웹 페이지에 매우 유용 → 클라이언트가 추가로 TCP 연결을 설정할 필요 없이 바로 요청할 수 있어서 응답이 더 빠름
- HTTP/1.1 의 기본 연결 방식: `Connection: Keep-alive`
- persistent HTTP response time = 1RTT + (1RTT * 객체의수) + file transmission time

# MVC (Model View Controller)

## MVC의 등장

### 초기 웹 개발

- PHP, JSP, ASP와 같은 기술 활용해 웹 애플리케이션 개발
- HTML과 프로그래밍 언어가 혼재되어 프로그래밍
- 초기 학습 비용이 낮고, 초반 개발 속도는 빠른 경향

> 🚨 웹 애플리케이션의 복잡도가 증가, 소스 코드 복잡도 증가하면서 자연스럽게 유지보수 비용이 많이 발생
>
>👉 한 곳에서 많은 로직을 처리하기보다는 역할을 분담하기 위해 등장한 것이 MVC

## MVC

### MVC의 역할 분담

- **Controller**: 최초 진입 지점. 사용자의 입력 값이 유효한 지를 검증하고, 사용자가 입력한 데이터를 Model에 전달하고, Model의 처리 결과에 따라 이동할 View를 결정하는 역할
- **Model**: 실질적인 비즈니스 로직을 구현하는 역할을 담당함. 비즈니스 로직 처리 결과를 DB에 저장하고 조회하는 역할. 애플리케이션의 엔진이라 할 수 있음.
- **View**: Controller에 의해 전달된 데이터를 단순히 출력하는 역할

### 프레임워크의 등장

- MVC 기반으로 개발한 결과 구현할 코드량도 많아지고 개발 생산성이 떨어지는 단점이 발생
- 이 같은 단점을 보완하기 위해 MVC 기반 개발을 지원하는 프레임워크가 등장
- 많은 기반 코드를 구현해 제공함으로써 개발자들이 구현할 부분을 최소화해 생산성을 높이는 효과

***
## Reference
[HTTP (HyperText Transfer Protocol) - Basics](https://www3.ntu.edu.sg/home/ehchua/programming/webprogramming/HTTP_Basics.html)
