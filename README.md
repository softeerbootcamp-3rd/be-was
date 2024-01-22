# Softeer bootcamp 3rd 학습사항 정리
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
> - entity-header : 요청하는 컨텐트와 관련된 정보 <br/>
>   ex) Allow, Content-Length, Content-Type, Content-Language, Content-Encoding
> <img width="571" alt="RequestMessage" src="https://github.com/SuHyeon00/be-was/assets/90602694/29786728-dee5-472b-86a0-e67b0052c207">

```
GET / HTTP/1.1
Host: localhost:8080
Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8
Connection: keep-alive
```
3. 
