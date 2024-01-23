# java-was-2023
Java Web Application Server 2023

## 프로젝트 정보
이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 를 참고하여 작성되었습니다.

## STEP 1 - index.html 응답

### Hardware Thread vs Software Thread
- 하드웨어 스레드<br/>
  : `운영체제`의 최소 작업 단위, CPU의 코어에서 동시에 할 수 있는 작업을 의미한다. 병렬적으로 몇 개의 소프트웨어 스레드를 수행할 수 있을지를 결정한다.<br/>
     하드웨어 스레드들은 병렬적(Parallelism)으로 수행된다.<br/>
- 소프트웨어 스레드<br/>
  : `응용 프로그램`이 갖는 최소 작업 단위, 시스템 자원 한도 내에서 계속해서 만들어낼 수 있다. 운영체제의 스케줄링 정책에 의해 병행적으로 (Concurrency) 수행된다. 즉, 여러개의 소프트웨어 스레드가 교차하여 실행되며 이를 문맥 교환 (Context Switching)이라 한다.

### HTTP 통신 방식
1. TCP 연결
2. 클라이언트에서 서버로 HTTP Request 메시지 전송
> RFC 표준
> - Request-Line : method uri http version
> - general-header : 요청과 응답 시 공통적으로 만들어지는 헤더 <br/>
>   ex) Cache-Control, Connection, Date ...
> - entity-header : 요청하는 컨텐츠와 관련된 정보 <br/>
>   ex) Allow, Content-Length, Content-Type, Content-Language, Content-Encoding ...
> <img width="571" alt="RequestMessage" src="https://github.com/SuHyeon00/be-was/assets/90602694/29786728-dee5-472b-86a0-e67b0052c207">

```
GET / HTTP/1.1
Host: localhost:8080
Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8
Connection: keep-alive
```
3. 서버 내에서 요청 처리 [(Spring Request 처리 과정)](#Spring-Request-처리-과정)
4. 서버에서 클라이언트로 Http Response 메시지 전송
> RFC 표준
> - Status-Line : http version statud code status message
> - general-header : 요청과 응답 시 공통적으로 만들어지는 헤더 <br/>
>   ex) Cache-Control, Connection, Date ...
> - response-header : 상태 줄에 포함되지 않은 서버에 대한 추가 정보 제공 <br/>
>   ex) Accept-Ranges, Age, Location, Proxy-Authenticate, Server ...
> - entity-header : 응답하는 컨텐츠와 관련된 정보 <br/>
>   ex) Allow, Content-Length, Content-Type, Content-Language, Content-Encoding ...
>   <img width="564" alt="ResponseMessage" src="https://github.com/SuHyeon00/be-was/assets/90602694/6cdf50d7-e304-42bf-9f07-2044aabdabab">

5. TCP 연결 끊거나 다른 요청을 위해 재사용

### Spring Requset 처리 과정
![spring_request](https://github.com/SuHyeon00/be-was/assets/90602694/744da5ae-46c9-4ea1-b2b7-66366caa0d80)
1. DispatcherServlet : 클라이언트 요청 받음, 네트워크 요청을 어떤 컨트롤러에게 위임할 지 탐색 (HandlerMapping 이용)<br/>
📌 HandlerMapping : 컨트롤러 탐색에 사용 (메소드 + url 두가지 필요)<br/>
📌 정적 자원 & 동적 자원 분할 처리 → RequestHandler에서 처리<br/>
: 컨트롤러 먼저 탐색 → 컨트롤러가 없다면 정적 자원 탐색<br/>
2. Controller : 위임 받은 요청을 처리할 서비스에게 로직 위임
3. Service : 비즈니스 로직 구현, DB 접근이 필요할 경우 DAO에 위임
4. DAO : 데이터 받아 서비스로 돌려줌 (Entity 객체 → DTO 변환)
5. 모든 로직 완료하면 Service → Controller 전달
6. Controller → DispatcheServlet으로 결과 (Model / View) 전달
7. DispatcheServlet → Client 응답 전송
