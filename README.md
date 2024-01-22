# Step-1

## Thread
### Multi-thread
- Multi-thread 환경에서 thread-safety는 race condition방지와 data consistency 관점에서 매우 중요하다.
  - thread safe code는 data integrity를 보장

### JAVA Concurrent Class
- synchronization mechanisms
  - thread-safe


## HTTP
### HTTP 메세지
1. 시작줄(start-line): 수행할 요청(Request), 혹은 요청된 수행에 대한 성공/실패(Response)를 기록함
2. Http Header 세트: 요청에 대한 설명, 메세지 본문에 대한 설명
3. 빈 줄(‘blank line’): 모든 메타 정보의 전송 완료를 알림
4. Body: 요청과 관련된 내용이나 응답과 관련된 문서

<img width="716" alt="스크린샷 2024-01-16 오전 10 23 58" src="https://github.com/ddrongy/be-was/assets/97080154/5ccc8382-ea08-454f-8c75-50d4317fb099">

## Http Request
### Request 시작 줄
- 서버가 특정 동작을 취하게 끔 만들기 위해 클라이언트에서 전송하는 메세지
1. Http Method: 영어 동사(GET, PUT, POST) 혹은 명사 (HEAD, OPTIONS)를 사용해 서버가 수행할 동작을 나타냄
2. 요청 타겟: 주로 URL, 프로토콜, 포트, 도메인의 절대 경로로 나타냄. 요청 타겟의 포멧은 HTTP 메소드에 따라 달라짐
- origin 형식
    - 가장 일반적인 형식으로 알려진 절대 경로
    - 형식의 끝에 ‘?’와 쿼리 문자열이 따라옴
    - http method와 함께 사용됨
- absolute 형식
    - 프록시에 연결하는 경우 대부분 GET과 함께 사용
- authority 형식
    - 도메인 이름 및 옵션 포트로 이뤄진 URL의 인증 컴포넌트
    - http 터널을 구축하는 경우 CONNECT와 함께 사용
- asterisk 형식
    - OPTIONS와 함께 ‘*’ 하나로 서버 전체를 나타내는 형식
3. HTTP 버전 : 응답 메세지에서 써야할 http 버전을 알려주는 역할

### Request Header
- 대소문자 구분없는 문자열 다음에 콜론(:)이 붙고, 그 뒤에 오는 값은 헤더에 따라 달라짐
- User-Agent(en-US), Accept 과 같은 Request 헤더는 요청의 내용을 좀 더 구체화 시킴
- 메세지 데이터의 원래 형식과 적용된 인코딩을 설명하는 Content-Type과 같은 Representation 헤더는 메세지 본문이 있는 경우에만 존재함

<img width="626" alt="스크린샷 2024-01-16 오전 10 43 58" src="https://github.com/ddrongy/be-was/assets/97080154/cb9accc6-9b33-4cf3-ac7b-d7a00ba5933f">

### Request Body
- GET, HEAD, DELETE, OPTIONS 처럼 리소스를 가져오는 요청은 보통 본문이 필요없음
- HTML폼 데이터를 요청하는 POST요청과 같은 일부 요청은 업데이트 하기 위해 서버에 데이터를 전송함
1. 단일-리소스 본문 : 헤더두개(Content-Type, Content-Length)로 구성
2. 다중 리소스 본문: HTML폼과 관련 있음

