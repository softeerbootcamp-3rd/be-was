# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 1. 학습 목표 
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수에 좋은 구조에 대해 고민하고 코드를 개선해 본다.

---

## 2. 기능 요구사항
1. 정적인 html 파일 응답
2. HTTP Request 내용 출력

---

## 3. 학습 내용 👨🏻‍💻

##### 1.Thread
- Concurrent 패키지의 ExecutorService를 사용하여 Thread를 관리한다.
  >- ExecutorService는 Thread를 생성하고 관리하는 클래스이며, Thread를 직접 생성하고 관리하는 것보다 편리하다.
  >- 스레드 풀은 내부적으로 스레드를 관리하여 병렬 처리를 지원한다.
  >- 고정 크기의 스레드 풀을 사용하면 스레드를 재사용할 수 있어 스레드 생성 및 소멸에 따른 오버헤드를 줄일 수 있다.

##### 2. HTTP Request 구조 분석
- HTTP Request는 Request Line, Request Header, Request Body로 구성된다.
  >- Request Line은 HTTP 메서드, 요청 URL, HTTP 버전으로 구성되며, 요청 URL을 통해 요청 대상을 식별한다.
- InputStream을 통해 Request Header를 읽어들인다.
  >- InputStream은 바이트 단위(byte[])로 데이터를 읽어들이는 스트림이다.
  >- BufferedReader는 문자 단위(char[])로 데이터를 읽어들이는 스트림이다.

##### 3. HTTP Response 구조 분석
- Status Line: Status Line은 HTTP 버전, 상태 코드, 상태 코드에 대한 설명으로 구성된다.

- Content-Type: 응답 본문의 타입을 나타낸다.
  >- text/html : html 파일
  >- text/css : css 파일
  >- application/javascript : js 파일
- Content-Length: 응답 본문의 길이를 나타낸다.
- body: 응답 본문을 나타낸다.
  >- html 파일을 읽어들여 byte[] 형태로 저장한다.

##### 4. OOP
- 객체 지향 프로그래밍의 특징인 응집도와 결합도를 고려하여 Util 클래스를 분리하였다.
  >- 응집도: 클래스나 모듈 안의 요소들이 얼마나 밀접하게 관련되어 있는지를 나타내는 척도로, 높은 응집도를 가지는 클래스는 하나의 책임만을 가지고 있다.
  >- 결합도: 하나의 클래스가 다른 클래스와 얼마나 많이 연결되어 있는지를 나타내는 척도, 낮은 결합도를 가지는 클래스는 다른 클래스와의 의존성이 낮다.
- ThreadUtil class: Thread를 생성하고 관리하는 클래스는 Thread를 생성하고 관리하는 책임을 가진다.
- FileUtil class: 파일의 경로 및 내용을 읽어들이는 책임을 가진다.

---

## 4. Trouble Shooting 🚀
- Content-Type 설정: html, css, js 파일을 읽어들여 응답 본문에 삽입할 때, Content-Type을 설정하지 않아서 파일이 정상적으로 읽어들여지지 않았다.
  >- Solution: file 확장자에 따라서 Content-Type을 설정하였다.
- InputStream을 통해 읽어들인 데이터를 문자 단위로 변환: InputStream을 통해 읽어들인 데이터를 문자 단위로 변환하지 않아서 읽어들인 데이터가 깨졌다.
  >- Solution: InputStream을 통해 읽어들인 데이터를 문자 단위로 변환하여 BufferedReader를 사용하였다.
  >- BufferedReader: 문자 단위로 데이터를 읽어들이는 스트림으로, Scanner 객체에 비해서 입출력 속도가 빠르다.<br>
     데이터를 한 곳에서 다른 한 곳으로 전송하는 동안 일시적으로 데이터를 보관하는 버퍼를 사용하므로 입출력 속도 향상


---

## 5. 추가로 학습할 내용 👨🏻‍💻
- 자바 스레드 모델에 대한 학습
  >- Thread 개념
  >- Green Thread 개념
  >- Native Thread 개념
  >- Virtual Thread 개념
- 학습 내용 정리: https://velog.io/@taegon1998/Thread