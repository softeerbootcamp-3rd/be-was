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

### 4. NIO vs. IO

- 공통점: 데이터를 입출력하기 위함
- 차이점

|구분| IO | NIO |
|--|----|-----|
|입출력 방식| 스트림| 채널  |
|버퍼 방식| Non-buffer |buffer|
|비동기 방식| 지원 안함 |지원|
|블로킹 / 넌블로킹| 지원 / 지원 X |지원 / 지원|

#### 4-2. IO
- 스트림
  - 입력 스트림과 출력 스트림이 구분, 따로 생성 필요
- Non-buffer
  - 출력 스트림이 1 byte를 쓰면 입력 스트림이 1 byte를 읽음
  - 느림
  - 읽은 데이터가 버퍼에 저장되지 않아 입력된 데이터의 위치를 이동해 가면서 자유롭게 이용할 수 없음
- 블로킹
  - 데이터를 읽을 시 데이터가 입력되기 전까지 스레드는 블로킹(대기상태)
  - 데이터 출력 전까지 스레드는 블로킹
  - IO 스레드가 블로킹되면 다른 일을 할 수 없음
  - 블로킹을 빠져나오기 위한 인터럽트(Interrupt)도 불가
  - 블로킹을 빠져나오는 유일한 방법은 스트림을 닫는 것
- 연결 클라이언트 수가 적고 전송되는 데이터가 대용량이면서 순차적으로 처리될 필요성이 있는 경우 주로 사용

#### 4-1. NIO
- NIO: New Input/Output
- 채널
  - 스트림과 달리 양방향으로 입력과 출력이 가능
  - 입력과 출력을 위한 별도의 채널을 만들 필요가 없음
- Buffer
  - 빠름
  - 읽은 데이터를 무조건 버퍼에 저장하기 때문에 버퍼 내에서 데이터의 위치를 이동해 가면서 필요한 부분만 읽고 쓸 수 있음
- 블로킹
  - 스레드를 인터럽트(Interrupt)함으로써 빠져나올 수 있음
- 넌블로킹
  - 입출력 작업 시 스레드가 블로킹되지 않는 것
  - 입출력 작업 준비가 완료된 채널만 선택 (셀렉터) 해서 작업 스레드가 처리하기 때문
- 불특정 다수의 클라이언트 연결 또는 멀티 파일들을 넌블로킹이나 비동기로 처리 가능
- 과도한 스레드 생성을 피하고 스레드를 효과적으로 재사용
- 연결 클라이언트 수가 많고, 하나의 입출력 처리 작업이 오래 걸리지 않는 경우 주로 사용

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

### 2. Enum
- 요청받은 URL에 따라 다른 함수를 호출해야함
- if 문을 통한 분기는 요청 URL이 추가될 때마다 조건문을 추가로 작성해야함
- Enum을 이용하면 요청 URL이 추가될 때마다 Enum 상수만 추가하면 됨
- 요청 처리: 상수별 호출할 메소드를 Service 클래스에 구현
- enum의 상수는 static final이므로 추상 메소드 구현 시 static method만 호출 가능
- enum은 상속 불가
 
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

-----------------------------------------------
## STEP 3. 다양한 컨텐츠 타입 지원

### 1. MIME
- Multipurpose Internet Mail Extensions의 약자
- 파일 변환을 의미
- 이메일과 함께 동봉할 파일을 텍스트 문자로 전환해서 이메일 시스템을 통해 전달하기 위해 개발
- 현재는 웹을 통해서 여러 형태의 파일을 전달하는데 쓰이고 있음
- 등장 배경
  - 예전에는 텍스트 파일을 주고받을 때 ASCII로 공통된 표준에 따르기만하면 문제가 없었음
  - 이후 ASCII 파일이 아닌 바이너리 파일을 보내는 경우가 생기게 되었음 (음악, 영상 등)
  - 이러한 바이너리 파일들을 문제 없이 전달하기 위해 텍스트 파일로 변환이 필요
  - 인코딩: 바이너리 파일 -> 텍스트 파일
  - 디코딩: 텍스트 파일 -> 바이너리 파일
- MIME으로 인코딩한 파일은 Content-type 정보를 앞부분에 담음

-------------------------------------------------

## 웹 서버 1주차 추가 공부

### 1. Java Reflection

- 프로그램이 자기 자신을 검사하고, 자신의 구조를 동적으로 분석하고 수정할 수 있는 능력
- 장점
  - 런타임에 동적 클래스 로딩 및 인스턴스 생성 가능
  - 메타데이터 얻기 가능
  - 유연한 코드 작성 -> 코드의 재사용성을 높이고 유지보수 용이
- 사용법
  1. Class 객체 얻기
  ```java
  Class<?> myClass = MyClass.class; // MyClass는 대상 클래스의 이름
  ```
   2. 클래스 정보 조회
  ```java
  // 클래스의 이름
  String className = myClass.getName();
  System.out.println("Class Name: " + className);
  
  // 클래스의 메서드들
  Method[] methods = myClass.getMethods();
  for (Method method : methods) {
      System.out.println("Method: " + method.getName());
  }
  
  // 클래스의 필드들
  Field[] fields = myClass.getDeclaredFields();
  for (Field field : fields) {
      System.out.println("Field: " + field.getName());
  }
  ```
   3. 인스턴스 생성
  ```java
  MyClass newInstance = (MyClass) myClass.newInstance();
  ```
   4. 메서드 호출
  ```java
  Method myMethod = myClass.getMethod("methodName", parameterTypes);
  myMethod.invoke(newInstance, arguments);
  ```
   5. 필드에 접근
  ```java
  Field myField = myClass.getDeclaredField("fieldName");
  myField.setAccessible(true);
  Object value = myField.get(newInstance);
  ```
  - setAccessible(true) 
    - 접근 제어자에 상관없이 해당 멤버(필드, 메서드, 생성자 등)에 접근할 수 있도록 접근성(accessibility)을 설정

  -----------------------------------------------
## STEP 4. POST로 회원가입하기

### 1. request body 추출

- http request를 bufferedreader로 읽는 과정에서, body가 포함되어 있을 경우 read 과정이 끝나지 않는 문제가 발생
- Header와 Body 사이의 CRLF를 받아온 뒤로 stream이 body를 받아오지 않고 어딘가에서 끝없이 대기
- 원인
  - readLine() 메소드는 EOF를 만나거나 \r, \n, \r\n 셋 중 하나를 만나야 문장의 끝이라고 판단
  - HTTP Request Message의 끝에는 캐리지 리턴이나 라인 피드가 없음
  - 따라서 bufferedreader는 문장이 끝났음을 인지하지 못하고 무한 대기하는 것
- 헤결 방법
  - request header 중 Content-Length가 존재
  - body의 길이를 나타내는 해당 값을 저장해두고, 해당 길이 만큼 body를 읽어와야 함
----------------------------------------------------
## STEP 5. 쿠키를 이용한 로그인

### 1. Cookie
- 하이퍼 텍스트의 기록서(HTTP)의 일종으로서 인터넷 사용자가 어떠한 웹사이트를 방문할 경우 그 사이트가 사용하고 있는 서버를 통해 인터넷 사용자의 컴퓨터에 설치되는 작은 기록 정보 파일
- 쿠키에 담긴 정보는 인터넷 사용자가 같은 웹사이트를 방문할 때마다 읽히고 수시로 새로운 정보로 바뀜
- 소프트웨어가 아니므로 컴퓨터 내에서 프로그램처럼 실행될 수 없으며 바이러스를 옮길 수도, 악성코드를 설치할 수도 없음
- 단, 스파이웨어를 통해 유저의 브라우징 행동을 추적하는데에 사용될 수 있음
- 누군가의 쿠키를 훔쳐서 해당 사용자의 웹 계정 접근권한을 획득할 수 있음
- 보안상 중요한 데이터는 저장하지 않음
- Key와 Value로 이루어진 데이터 
- 주로 쿠키는 서버에서 생성하여 Set-Cookie HTTP Response Header에 넣어 클라이언트에게 전달
- 클라이언트에서 직접 쿠키에 데이터를 저장 할 수도 있음
- 유저의 별다른 컨트롤 없이, 요청 시 자동으로 request header에 넣어서 서버에 전송
- 만료기간을 설정할 수 있으며, 만료기간 전까지 브라우저가 종료되어도 삭제되지 않음

1. key-value
   - value에 실제 데이터가 담겨져 있음
   - key값으로 쿠키의 value값을 조회, 갱신, 삭제 가능
2. path
   - 특정 경로에서만 쿠키를 활성화
3. domain
   - 쿠키가 전송되어질 서버 도메인을 지정
   - 해당 도메인 및 하위 도메인에서만 쿠키에 접근 가능
4. expires
   - 쿠키의 만료기간
   - 만료기간이 지나면 쿠키 자동 삭제
5. secure
   - secure 속성을 설정하면 HTTPS 통신이 아닌경우 서버로 쿠키를 전송하지 않음

-----------------------------------------------
## 웹 서버 2주차 추가 공부

1. trim() vs. strip()
- 공통점: 문자열의 앞뒤 공백 제거
- 차이점
  - trim(): '\u0020' 이하의 공백들만 제거 (스페이스, 탭 등)
  - strip(): 유니코드의 공백들을 모두 제거 -> 더 많은 종류의 공백 제거 가능

2. isEmpty() vs. isBlack()
- 공통점: 문자열이 empty string인지 체크
- 차이점
  - isEmpty(): 문자열의 길이가 0인 경우 true를 리턴
  - isBlank(): 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면 true를 리턴
    -> white space가 포함되어 있어도 true 
