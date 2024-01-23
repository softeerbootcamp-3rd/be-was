## 1. 학습 목표

- 쿠키와 세션을 이용한 로그인 방식을 이해하고 직접 구현할 수 있다.

---

## 2. 기능 요구사항

- 가입한 회원 정보로 로그인을 할 수 있다.
- [로그인] 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
- 로그인이 성공하면 index.html로 이동한다.
- 로그인이 실패하면 /user/login_failed.html로 이동한다.

---

## 3. 학습 내용

### 1. HTTP Cookie

- 개요
  > HTTP 쿠키(웹 쿠키, 브라우저 쿠키)는 서버가 사용자의 웹 브라우저에 전송하는 데이터로,
  > 서로 다른 두 요청이 동일한 브라우저에서 들어왔는지 아닌지를 판단할 때 주로 사용합니다.

- 쿠키 만들기
  > HTTP 요청을 수신할 때, 서버는 응답과 함께 Set-Cookie 헤더를 전송할 수 있습니다.
  > 쿠키는 보통 브라우저에 의해 저장되며, 그 후 쿠키는 같은 서버에 의해 만들어진 요청(Request)의 Cookie HTTP 헤더안에 포함되어 전송됩니다.
  >
  > 만료일 혹은 지속시간(duration)도 명시될 수 있고, 만료된 쿠키는 더이상 보내지지 않습니다.

- 헤더 구조
  > - Response - `Set-Cookie: <cookie-name>=<cookie-value>`
  > - Request - `Cookie: <cookie-name>=<cookie-value>`

- Path
  > Cookie 헤더를 전송하기 위하여 요청되는 URL 내에 반드시 존재해야 하는 URL 경로입니다.
  >
  > "/" 문자를 통해 디렉토리를 구분합니다.

- 쿠키의 라이프 타임
  > - 현재 세션이 끝날 때 삭제
  > - 명시된 날짜/기간 이후 삭제

---

## 4. 문제 해결

---

## 5. 기록

---

## 6. 참고 문서

[MDN - Using HTTP cookies](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies)
