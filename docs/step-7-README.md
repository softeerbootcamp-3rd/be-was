# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was
를 참고하여 작성되었습니다.

## 1. 학습 목표
- HTML Form과 HTTP POST를 활용하여 글쓰기 기능을 구현한다.
- 지금까지 학습한 내용을 토대로 게시판 기능을 완성한다.

---

## 2. 기능 요구사항
1. 글쓰기 기능 구현
    - index.html 하단에 글쓰기 버튼을 추가한다.
    - 로그인한 사용자가 글씨기 버튼을 클릭하면 write.html 로 이동한다.
    - 로그인하지 않은 사용자가 글쓰기 버튼을 클릭하면 로그인 페이지로 이동한다.
    - write.html에서는 글의 본문을 입력할 수 있는 폼을 표시한다.
    - 작성한 글의 제목을 index.html에서 보여준다.

2. 글쓰기에 파일 첨부 기능 추가
    - 글쓰기 페이지에 첨부 파일 업로드 기능을 추가한다.

3. 세부 글 표시 기능 구현
    - 로그인한 사용자가 글 제목 클릭시 세부 내용을 볼 수 있는 페이지로 이동한다.
    - 로그인하지 않은 사용자가 글 제목 클릭시 로그인 페이지로 이동한다.
    - (선택) 첨부 파일이 이미지일 경우 미리 보기 기능이 동작하도록 구현한다.
4. 404 및 기타 에러 처리 페이지 구현
    - 존재하지 않는 URL 요청에 대해 404 페이지로 이동하도록 구현한다.
    - 처리할 수 없는 메소드 요청에도 적절한 에러 페이지(405 등)으로 응답하도록 구현한다.
    - 기타 발생한 서버 오류를 확인할 수 있는 에러 페이지를 구현한다.

5. DB 연동
    - H2와 JDBC를 사용하여 회원정보와 게시글을 저장하도록 구현한다.

6. POST 파일 업로드 처리
    - 파일 업로드가 정상적으로 동작하기 위해서 사용자가 업로드한 파일을 POST로 처리할 수 있도록 구현한다.



---

## 3. 학습 내용 👨🏻‍💻

##### 1. Multipart/form-data
- HTTP를 통해 파일을 업로드할 때, `multipart/form-data`는 파일이나 데이터를 분리된 여러 부분으로 전송하는 포맷이다.
- HTML `<form>` 태그와 함께 사용되며, `<form>`의 `enctype` 속성이 `multipart/form-data`로 설정됐을 때 사용된다.
  - **Content-Type 헤더**: `multipart/form-data`를 명시하여 HTTP 요청이 여러 데이터 부분으로 구성됨을 알림
  - **Boundary 문자열**: `boundary=` 파라미터에 이어진 고유 문자열로 각 파트를 구분하는 구분자 역할
 - 파일 업로드 HTTP 요청 메시지 예시
    ```http
    POST /upload HTTP/1.1
    Host: example.com
    Content-Type: multipart/form-data; boundary=AaB03x
    
    --AaB03x
    Content-Disposition: form-data; name="file"; filename="example.jpg"
    Content-Type: image/jpeg
    
    [바이트로 인코딩된 이미지 데이터]
    --AaB03x--
    ```
 

---

## 4. Trouble Shooting 🚀
- **BufferedReader와 바이트 데이터 처리**: HTTP 요청을 통해 바이트 데이터를 받을 때, BufferedReader를 사용했다. <br>
Byte로 읽어들인 데이터를 BufferedReader를 통해 String으로 변환하면서 데이터가 손실되어 파일이 깨지는 현상이 발생했다. <br>
  >- 원인: BufferedReader는 텍스트 데이터 처리에 적합하지만 바이너리 데이터를 다룰 때는 InputStream 같은 바이트 기반 스트림을 사용해야 한다.
  >- 해결 방법
  >  1. 서버로부터 받은 HTTP 요청의 바디에서 바이트 데이터를 읽는다.
  >  2. InputStream을 통해 데이터를 바이트 단위로 읽고, 읽어들인 바이트 데이터를 FileOutputStream을 통해 파일로 작성한다.
  >  3. 모든 작업이 끝나면 스트림을 닫아 자원을 해제한다.
  >- 자바 코드 예시
    >  ```java
    >  InputStream inputStream = request.getInputStream();
    >  FileOutputStream fileOutputStream = new FileOutputStream("/path/example.jpg");
    >  
    >  int bytesRead;
    >  byte[] buffer = new byte[4096];
    >  while ((bytesRead = inputStream.read(buffer)) != -1) {
    >  fileOutputStream.write(buffer, 0, bytesRead);
    >  }
    >  
    >  fileOutputStream.close();
    >  inputStream.close();
    >  ```
----

## 5. be-was 프로젝트를 마치며 👋🏻
- 약 2주가 조금 넘는 기간동안 프로젝트를 진행했다. <br>
첫째날과 둘째날에는 어떻게 구현하지? 라는 생각이 가장 많이 들었고, 익숙해지자 그때부터는 어떻게 보다는 어떤 방식으로 구현해야 좋은 코드가 될까? 라는 생각을 하게 되었다.<br>
스프링부트로 항상 기술을 사용만 했었는데, 자바로 WAS를 직접 구현하면서 자바의 기본적인 개념들과 HTTP 프로토콜에 대해 생각보다 더 깊게 이해할 수 있었다.<br>
스프링부트에 고마움을 느끼면서도, 기술을 가져다가 사용만 했던 모습에 대해 반성하게 되었다.<br>
피어세션이라는 새로운 경험을 통해 동기들의 좋은 코드 또는 좋은 학습 방법을 배우기도 하고, 나의 코드를 동료들에게 리뷰받으며 내가 작성한 코드에 대해 깊게 고민할 수 있던 있었다.<br>
이슈가 발생하면, 항상 본인 일처럼 같이 고민해주고 해결해주려고 노력했던 그동안 만났던 팀원들에게 너무 고맙다!
