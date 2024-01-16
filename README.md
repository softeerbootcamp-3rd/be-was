# # 웹 서버
### 1. WebServer 클래스
- 웹 서버를 시작하며, 클라이언트의 요청이 있을 때까지 대기
- 요청이 들어올 경우 해당 요청을 RequestHandler 클래스에게 위임
- 새로운 thread를 실행

### 2. RequestHandler 클래스
- WebServer 클래스로부터 전달받은 Socket을 이용하여 클라이언트의 요청을 처리

-------------------------
## STEP 1. index.html 응답
### 1. HTTP Request Parsing
- BufferedReader를 이용하여 Request header를 읽어옴
- 파일 종류에 따른 HTTP Request 파싱<br>

| 파일 종류 | 파일 경로 | Accept   |
|-------|-------|----------|
| .html |./src/main/resources/templates| text/html |
| .css  |./src/main/resources/static| text/css |
| fonts |./src/main/resources/static| \*/\*    |
| .png  |./src/main/resources/static| \*/\*|
| .js   |./src/main/resources/static|\*/\*|
| .ico  |./src/main/resources/static|image/avif|

### 2. ExecutorService Interface
- 동시에 여러 요청을 처리해야 하는 경우 매번 새로운 스레드를 만드는 것은 비효율적<br>
-> Thread 개수 증가 -> CPU 과부하 -> 메모리 사용량 증가
- Thread Pool: 스레드를 미리 만들어두고 재사용
- Executor 인터페이스: 등록된 작업의 실행만을 책임짐
- ExecutorService 인터페이스: 작업의 등록 + 실행을 책임짐
- submit() vs. execute()
  - 공통점: Thread Pool 객체가 Thread를 실행하고자 할 때 사용
  - 차이점: 
    1. execute()
       - return 값 없음
       - 작업 처리 중 예외 발생 시 스레드 종료 -> 스레드 풀에서 삭제 -> 다른 스레드 생성
    2. submit()
       - Future 클래스 객체 (비동기 테스크의 결과) 반환
       - 작업 처리 중 예외 발생 시 스레드 종료 X -> 다음 작업에서 재사용

### 3. 자바의 Stream

#### 1) byte 기반 Stream
- InputStream
  - 바이트 기반 입력 스트림 최상위 추상 클래스
  - 하위 클래스: XXXInputStream
  - 클라이언트에서 서버로 요청을 보낼 때 전달되는 데이터를 담당하는 스트림
  - 입력받은 데이터는 int 형으로 저장되며 이는 10진수의 UTF-16 값으로 저장됨
  - 1 byte만 읽음
- OutputStream
  - 바이트 기반 출력 스트림 최상위 추상 클래스
  - 하위 클래스: XXXOutputStream
  - 서버에서 클라이언트로 응답을 보낼 때 전달하는 데이터를 담당하는 스트림
- DataOutputStream
  - DataOutputStream(OutputStream out)<br>
    -> 주어진 OutputStream 인스턴스를 기반스트림으로 하는 DataOutputStream 인스턴스를 생성
#### 2) 문자 기반 Stream
- Reader
  - 문자 기반 입력 스트림 최상위 추상 클래스
  - 하위 클래스: XXXReader
- InputStreamReader
  - InputStream이 저장한 바이트 단위 데이터를 문자 단위(character) 데이터로 변환
  - char 배열로 데이터를 받을 수 있음

- Writer
  - 문자 기반 출력 스트림 최상위 추상 클래스
  - 하위 클래스: XXXWriter
### * 기타 사항
Q. 요청을 한 번만 보냈음에도 추가 요청이 들어오는 이유는?<br>
A. 서버가 웹 페이지를 구성하는 모든 리소스를 한번의 응답으로 보내지 않기 때문<br>
-> 다른 자원(css, js...)이 포함되어 있을 경우 서버에 해당 자원을 다시 요청<br>
-> 여러 개의 Thread가 생성됨<br>
<br>
Q. 요청을 잘 받았음에도 html을 반환하면 해당하는 css가 반영이 안되는 이유는?<br>
A. 파일 종류에 따라 응답 헤더의 content type을 다르게 지정해주어야함.
