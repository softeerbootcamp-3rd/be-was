# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was
를 참고하여 작성되었습니다.

## 1. 학습 목표
- 쿠키와 세션을 이용한 로그인 방식을 이해하고 직접 구현할 수 있다.

---

## 2. 기능 요구사항
1. 가입한 회원 정보로 로그인을 할 수 있다.

2. [로그인] 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.

3. 로그인이 성공하면 index.html로 이동한다.

4. 로그인이 실패하면 /user/login_failed.html로 이동한다.

---

## 3. 학습 내용 👨🏻‍💻

##### 1. Cookie & Session
- Cookie: 서버가 클라이언트에게 제공하는 작은 데이터 조각
- Cookie + Session 동작 과정
  >1. 클라이언트가 서버에 요청한다.
  >2. 서버는 Response Header에 Set-Cookie를 통해 session-id를 발급한다.
  > session-id는 서버에서 관리되며, session-id를 통해 클라이언트를 구분할 수 있다.
  >3. 클라이언트는 Cookie 저장소에 session-id를 저장한다.
  >4. 클라이언트는 서버에 요청할 때마다 Cookie를 전송한다.
  >5. 서버는 Cookie에 담긴 session-id를 통해 클라이언트를 구분하고 요청에 응답한다.

- Cookie 종류
  >1. Session Cookie: 브라우저가 종료되면 삭제되는 Cookie
  >   - Expires 또는 Max-Age 속성을 설정하지 않아 브라우저가 종료되면 쿠키가 만료
  >2. Persistent Cookie: 브라우저가 종료되어도 삭제되지 않는 Cookie
  >   - Expires 또는 Max-Age 속성을 지정하여 특정 시간이 지나면 만료되는 쿠키로, 브라우저를 닫아도 만료 기간이 지나지 않는 한 계속해서 유지

- Cookie 속성
  >1. Secure 속성: 쿠키가 HTTPS 연결을 통해서만 전송될 수 있도록 제한
  >2. HttpOnly 속성: 쿠키에 접근할 수 없도록 제한
  >   - js의 document.cookie와 같이 클라이언트에서 쿠키에 접근하는 것을 막기 위해 사용

- Cookie 특징
  >1. 쿠키는 클라이언트에 저장되기 때문에 클라이언트에서 쿠키를 조작할 수 있음 (보안에 취약함)
  >2. 쿠키는 클라이언트에 저장되기 때문에 클라이언트의 요청마다 쿠키를 전송하므로 네트워크 트래픽이 증가할 수 있음

- **Cookie + Session 방식을 사용하는 이유**: 사용자의 민감한 정보를 클라이언트에 저장하지 않고, 세션을 통해 서버에 안전하게 유지할 수 있다.
  >1. 쿠키에는 세션ID만 저장
  >2. 서버는 세션ID를 기반으로 세션 저장소에서 사용자의 정보를 저장
  >   - 민감한 정보를 클라이언트에 저장하지 않고, 서버에 저장하므로 보안에 유리


##### 2. Storage
- Storage: 클라이언트에 데이터를 저장할 수 있는 브라우저 내 저장소
- Storage 종류
  >1. Local Storage: 브라우저를 종료해도 데이터가 유지되는 저장소
  >   - key-value 형태로 데이터를 저장
  >   - origin에 종속적이므로 동일한 origin에서만 접근 가능
  >   - 만료 기간이 없으므로 브라우저나 탭을 종료해도 데이터가 유지
  >   - 서버에 자동으로 전송되지 않고, js를 통해 직접 전송해야 함
  >   - 활용: 사용자의 환경 설정, 게임의 진행 상태 등
  >2. Session Storage: 브라우저를 종료하면 데이터가 삭제되는 저장소
  >   - key-value 형태로 데이터를 저장
  >   - origin에 종속적이므로 동일한 origin에서만 접근 가능
  >   - 브라우저나 탭을 종료하면 데이터가 삭제
  >   - 서버에 자동으로 전송되지 않고, js를 통해 직접 전송해야 함
  >   - 활용: 로그인 정보, 장바구니 등
  >3. Cookie Storage: Cookie를 저장하는 저장소
  >   - key-value 형태로 데이터를 저장
  >   - path를 통해 origin에 종속적이지 않게 설정 가능
  >   - 만료 기간을 설정하여 브라우저나 탭을 종료해도 데이터가 유지할 수 있고, 만료 기간이 없으면 브라우저나 탭을 종료하면 데이터가 삭제
  >   - 서버에 자동으로 전송

---

## 4. Trouble Shooting 🚀
- 필드값이 empty인 경우 예외 처리가 되지 않는 문제
  >- 원인: split을 통해 key-value를 분리할 때, value가 empty인 경우 ArrayIndexOutOfBoundsException이 발생
  >- 해결: -1 옵션을 주어 value가 없는 경우에도 empty String을 반환하도록 수정하였다. <br>
  그 이후, value가 empty인 경우 예외 처리를 하여 회원가입 또는 로그인 실패 페이지로 이동하도록 수정하였다.
  >   - -1 옵션: 문자열을 분리할 때, 갯수 제한을 두지 않고 모두 분리하도록 하는 옵션 (공백 문자열도 반환)
  >   - split 함수는 끝에 공백 문자열이 있을 경우, 공백 문자열을 반환하지 않는다. (공백 문자열을 반환하려면 -1 옵션을 주어야 한다.)
---

## 5. 추가로 학습할 내용 👨🏻‍💻
- 다양한 로그인 방식
  >1. Cookie + Session 방식
  >2. JWT 방식

- 학습 내용 정리: https://velog.io/@taegon1998/%EB%8B%A4%EC%96%91%ED%95%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EB%B0%A9%EC%8B%9D
