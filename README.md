# 📌 java-was-2023

Java&#123;Web Application Server 2023

## ✅ 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## ✅ 학습한 내용
🔹 [좋은 커밋 메세지](https://github.com/DSL2e/BE-hyundai-1/wiki/%EC%A2%8B%EC%9D%80-%EC%BB%A4%EB%B0%8B-%EB%A9%94%EC%8B%9C%EC%A7%80)

🔹 [HTTP](https://github.com/DSL2e/BE-hyundai-1/wiki/HTTP)

🔹 [Gradle project 구조](https://github.com/DSL2e/BE-hyundai-1/wiki/Gradle-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0)

🔹[1주차 피드백](https://github.com/DSL2e/BE-hyundai-1/wiki/1%EC%A3%BC%EC%B0%A8-%ED%94%BC%EB%93%9C%EB%B0%B1)

🔹[Concurrnet](https://github.com/DSL2e/BE-hyundai-1/wiki/Java-Concurrent)

🔹[MIME 타입](https://github.com/DSL2e/BE-hyundai-1/wiki/MIME-%ED%83%80%EC%9E%85)

🔹[JVM 작동원리](https://github.com/DSL2e/BE-hyundai-1/wiki/JVM%EC%9D%98-%EB%8F%99%EC%9E%91-%EC%9B%90%EB%A6%AC)

## ✅ STEP1(index.html 응답)

### ✔ 기능 요구 사항

#### 1. 정적인 html 파일 응답
http://localhost:8080/index.html 로 접속했을 때 src/main/resources/templates 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

#### 2. HTTP Request 내용 출력
서버로 들어오는 HTTP Request의 내용을 읽고 적절하게 파싱해서 로거(log.debug)를 이용해 출력한다.

### ✔ 구현 방법
>⚪ port번호를 명령인수로 주지 않는다면 port번호를 8080으로 설정. ExecutorService를 이용해 스레드 풀 생성한 후 서버 소켓을 열고 대기

>⚪ 클라이언트 연결이 되었다면 RequestHandler 객체를 실행하고 inputstream를 BufferedReader으로 저장

>⚪ HttpRequest를 분석하여 RequestHeader와 Parameter를 HttpRequest 객체에 저장

>⚪ HttpRequest에 저장된 method와 path를 바탕으로 해당 과정을 처리하는 controller 생성 및 service 실행

>⚪ path를 통해 파일을 읽어 저장 및 content-type, stautscode 등 필요한 내용을 HttpResponse에 저장

>⚪ HttpHandler에서 HttpResponse 내용을 바탕으로 header와 body를 생성하여 전송

## ✅ STEP2(웹 서버 2단계 - GET으로 회원가입)

### ✔ 기능 요구 사항

#### 1. GET으로 회원가입 기능 구현
“회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동, 회원가입 폼을 표시한다.
이 폼을 통해서 회원가입을 할 수 있다.

### ✔ 구현 방법
>⚪ STEP1의 방식으로 HttpRequest를 파싱하고 이를 분석하여 HttpResponse를 만들고 이를 통해 Header과 body를 만들어 전송

>⚪ 회원가입 폼에서 가입을 누르면 /create?userId=&password=&name=&email= 형식으로 path가 설정되어 request

>⚪ HttpRequest를 파싱하고 path가 /create라면 UserCreateController에서 service 실행

>⚪  Path를 분석하여 인코딩되어 있는 정보를 디코딩하여 User객체에 파싱하고 database에 저장



## ✅ STEP3(다양한 컨텐츠 타입 지원)

### ✔ 기능 요구 사항
지금까지 구현한 소스 코드는 stylesheet 와 파비콘 등을 지원하지 못하고 있다. 다양한 컨텐츠 타입을 지원하도록 개선해 본다.

### ✔ 구현 방법
>⚪  STEP1에서 구현하였는데, request에서 path에 따른 content-type를 responseHeadr에 담아서 전송


## ✅ STEP4(POST로 회원 가입)

### ✔ 기능 요구 사항
로그인을 GET에서 POST로 수정 후 정상 동작하도록 구현하고, 가입을 완료하면 /index.html 페이지로 이동한다.

### ✔ 구현 방법
>⚪ Request Body의 값을 추출하기 위해 HttpRequest에 Content-type이 있다면 content-length를 저장하여 해당 길의 만큼 읽어서 저장

>⚪ 이를 Map형식으로 저장한 후, HttpRequest의 prameter에 저장하여 STEP2와 마찬가지로 해당 데이터를 통해 db에 저장 

>⚪ status code = 302, location = /index.html로 HttpResponse를 저장하고 전송 

## ✅ STEP5(쿠키를 이용한 로그인)

### ✔ 기능 요구 사항
1. 가입한 회원 정보로 로그인을 할 수 있다.
2. [로그인] 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
3. 로그인이 성공하면 index.html로 이동한다.
4. 로그인이 실패하면 /user/login_failed.html로 이동한다.

### ✔ 구현 방법
>⚪ /user/login.html에서 로그인을 누르면 method:POST,path:/user/login로 HttpRequest를 함

>⚪ path:/user/login이라면 loginController의 service를 실행하여 db에 해당 유저가 있는지 확인하고 있다면 password 비교

>⚪ 검증이 되었다면 세션을 생성하여 UUID.randomUUID()를 통해 SID를 만들고 세션 만료 시간을 설정

>⚪ 쿠키 설정 - path를 '/'로 설정하여 모든 요청에 처리가능함 (Set-Cookie: sid=; Expires=; Path='/')

>⚪ status=302, location=로그인여부에 따라 설정, 쿠키 정보를 담아서 Httpresponse를 만들어 전송 


## ✅ STEP6(동적인 HTML)

### ✔ 기능 요구 사항
1. 사용자가 로그인 상태일 경우 /index.html에서 사용자 이름을 표시해 준다.
2. 사용자가 로그인 상태가 아닐 경우 /index.html에서 [로그인] 버튼을 표시해 준다.
3. 사용자가 로그인 상태일 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
4. http://localhost:8080/user/list  페이지 접근시 로그인하지 않은 상태일 경우 로그인 페이지(login.html)로 이동한다.

### ✔ 구현 방법
>⚪ httpRequest에 cookie에서 SID value가 있다면 로그인 상태라고 판단하였음

>⚪ index.html의 해당 위치에 {{login}}를 추가하고 로그인 여부에 따라 동적으로 html파일 수정

>⚪ html파일이라면 htmlController의 service 실행하여 동적으로 html파일 수정하는 기능 수행

>⚪ 아래와 같이 content객체에 StringBulider를 통해 로그인 여부에 따른 내용을 정의해두고 {{login}}와 replace한 후 response body에 저장
```angular2html
// Content 객체 - 로그인 하였을 때
stringBuilder
    .append("<li>").append("<a href=\"#\" role=\"button\">").append(userId).append("</a>").append("</li>\n")
    .append("<li>").append("<a href=\"#\" role=\"button\">").append("로그아웃").append("</a>").append("</li>\n")
    .append("<li>").append("<a href=\"#\" role=\"button\">").append("개인정보수정").append("</a>").append("</li>\n");
```
>⚪ Request - path:/user/list일 때, UserListController의 service 실행하여 /user/list.html로 redirection

>⚪ 다시 클라이언트가 list.html로 요청했을 때 html파일이므로 HtmlController로 이동

>⚪ list일 때 db의 전체 user정보를 넘겨주어 아래와 같이 미리 저장된 형식에 맞게 StringBuilder로 생성 후 {{list}}와 replace한 후 response body에 저장
```angular2html
for (User user : users)
    stringBuilder.append("<tr>\n")
        .append("<th scope=\"row\">").append(index++).append("</th>")
        .append("<td>").append(user.getUserId()).append("</td>")
        .append("<td>").append(user.getName()).append("</td>")
        .append("<td>").append(user.getEmail()).append("</td>")
        .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
        .append("</tr>\n");
```