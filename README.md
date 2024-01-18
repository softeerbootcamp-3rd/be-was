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
  - flush(): 버퍼에 남은 데이터를 모두 출력시키고 버퍼를 비우는 역할
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

-----------------------------
## STEP 2. GET으로 회원가입

### 1. 요청 URL 디코딩
- 요청으로 들어오는 URL에 한글이 포함되어 있는 경우, 브라우저에서 인코딩한 상태로 전달
- 원하는 한글의 모습으로 반환하기 위해 디코딩 과정 필요
- URLDecoder클래스의 decode 함수를 사용

### 2. Enum을 사용한 분기 처리
- 요청받은 URL에 따라 다른 함수를 호출해야함
- if 문을 통한 분기는 요청 URL이 추가될 때마다 조건문을 추가로 작성해야함
- Enum을 이용하면 요청 URL이 추가될 때마다 Enum 상수만 추가하면 됨
- 상수별 호출할 메소드를 Service 클래스에 구현
 
### 3. JUnit
1. given-when-then 패턴
- given(준비): 어떠한 데이터가 준비되었을 때
- when(실행): 어떠한 함수를 실행하면
- then(검증): 어떠한 결과가 나와야 한다
2. 자바에서 단위테스트를 수행하기 위한 테스트 프레임워크
3. gradle 프로젝트의 경우 build.gradle에 의존성 추가
```
dependencies {
  testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
  testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}
```
4. JUnit 기본 assert 메소드

|형태|설명|
|---|---|
|void assertTrue(boolean condition)|condition이 true이면 테스트에 성공한다.|
|void assertEquals(Object expected, Object actual)|expected와 actual의 값이 같으면 테스트에 성공한다.|
|void assertArrayEquals(byte[] expected, byte[] actual)|expected와 actual의 내용이 정확히 같으면 테스트에 성공한다.
|void assertSame(Object expected, Object actual)|expected와 actual이 같은 레퍼런스이면 테스트에 성공한다.|
|void assertNull(Object actual)|actual이 null이면 테스트에 성공한다.|
|void assertAll(Executable... executables)|인자로 들어온 모든 executable들이 예외를 발생시키지 않으면 테스트에 성공한다.|

5. JUnit 기본 어노테이션

|어노테이션| 설명                                            |
|-------|-----------------------------------------------|
| @Test| 테스트 메소드를 나타내는 어노테이션, 필수                       |
|@BeforeEach| 각 테스트 메소드 시작 전에 실행되어야 하는 메소드에 써준다.            |
|@AfterEach	| 각 테스트 메소드 종류 후에 실행되어야 하는 메소드에 써준다.            |
|@BeforeAll| 테스트 시작 전에 실행되어야 하는 메소드에 써준다. (static 메소드여야 함) |
|@AfterAll| 테스트 종료 후에 실행되어야 하는 메소드에 써준다. (static 메소드여야 함) |
|@Disabled| 실행되지 않아야 하는 테스트 메소드에서 써준다.                    |

### 4. AssertJ
- usingRecursiveComparison: 실제 객체와 expected 객체의 필드를 재귀적으로 비교