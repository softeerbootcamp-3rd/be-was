# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.
-------------------------------

## 1. 웹 서버 1단계 - index.html 응답
### 1. WebServer 클래스
- 웹 서버를 시작하며, 클라이언트의 요청이 있을 때까지 대기
- 요청이 들어올 경우 해당 요청을 RequestHandler 클래스에게 위임
- 새로운 thread를 실행

### 2. RequestHandler 클래스
- WebServer 클래스로부터 전달받은 Socket을 이용하여 클라이언트의 요청을 처리
- InputStream:
  - 클라이언트에서 서버로 요청을 보낼 때 전달되는 데이터를 담당하는 스트림
- OutputStream:
  - 서버에서 클라이언트로 응답을 보낼 때 전달하는 데이터를 담당하는 스트림
### 3. 구현 방법
- BufferedReader를 이용하여 Request header를 읽어옴
- split 함수를 이용하여 공백을 기준으로 request line 쪼개기
- path에 해당하는 파일 데이터를 byte 배열로 읽어 반환
### 4. 기타 사항
Q. 요청을 한 번만 보냈음에도 추가 요청이 들어오는 이유는?<br>
A. 서버가 웹 페이지를 구성하는 모든 리소스를 한번의 응답으로 보내지 않기 때문<br>
-> 다른 자원이 포함되어 있을 경우 서버에 해당 자원을 다시 요청<br><br>
Q. 요청을 잘 받았음에도 html을 반환하면 해당하는 css가 반영이 안되는 이유는?<br>
A. response200Header 함수에서 writeBytes 할 때 파일 종류에 따라 content type을 다르게 지정해주어야함.
