## 1. 학습 목표

- HTTP GET 프로토콜을 이해한다.
- HTTP GET에서 parameter를 전달하고 처리하는 방법을 학습한다.
- HTTP 클라이언트에서 전달받은 값을 서버에서 처리하는 방법을 학습한다.

---

## 2. 기능 요구사항

- GET으로 회원가입 기능 구현

---

## 3. 학습 내용

### 1. HTTP GET Method

- GET 메서드
  > HTTP GET 요청 메서드는 서버로부터 자원을 요청하는 데 사용됩니다.
  > <br>GET 요청은 데이터를 가져올 때만 사용해야 하며 서버는 상태를 변경하면 안 됩니다.

- Query Parameter
  > GET /user/create?`userId=id&password=pwd&name=user&email=user@email`
  >
  > 쿼리 파라미터는 GET 요청을 하면서 파라미터를 서버에 보내줄 수 있는 방법입니다.
  > <br>주소와 쿼리 파라미터는 `?`로 구분됩니다.
  > <br>파라미터는 `{key}={value}` 형태로 작성하며, `&`를 사용하여 구분합니다.

- final 키워드
  > Java가 제공하는 불변성 키워드입니다.
  > 클래스나 변수에 사용할 경우 처음 정의된 상태가 변하지 않는 것을 보장할 수 있습니다.

- HTTP Status Code
  > 특정 HTTP 요청이 성공적으로 완료되었는지 알려주는 HTTP 상태 코드는 5가지 그룹으로 나뉘며, 그룹마다 세부적으로 구분됩니다.
  > 
  > 또한 상태코드마다 헤더의 내용이 달라질 수 있습니다.

- URI, URL, URN
  > URI는 통합 지원 식별자의 줄임말로, 인터넷에 있는 자원을 나타내는 유일한 주소입니다.
  > 
  > 하위 개념으로는 URL과 URN이 있으며,
  > <br>URL은 어떻게 리소스를 얻을 것이고 어디에서 가져와야하는지 명시하는 URI이고,
  > <br>URN은 리소스를 어떻게 접근할 것인지 명시하지 않고 경로와 리소스 자체를 특정하는 것을 목표로하는 URI입니다.

- try-with-resources
  > try-catch-finally에서 자원을 직접 반납해주는 번거로움과 실수로 인한 오류 발생을 해결하기 위해 Java7부터 도입된 문법입니다.
  > 
  > AutoCloseable 인터페이스를 구현하고 있는 자원에 대해 적용 가능합니다.

- Parameterized Test
  > 다양한 매개변수를 사용하여 단일 테스트 방법을 여러 번 실행할 수 있게 해주는 기능입니다.
  > 
  > 이 기능을 사용하여 UserService 클래스의 saveUser 메서드를 테스트하고자 했지만 다른 결과를 예상하는 경우 
  > 결과에 대한 값도 인자로 넣어야 한다는 점을 확인했습니다.
  > <br>따라서 상황에 따라 Test와 Parameterized Test를 사용하는 방식을 선택하기로 했으며, 
  > UserService의 saveUser 메서드는 Test로 테스트하기로 했습니다.
  > 

---

## 4. 문제 해결

- HTTP Redirect
  > 회원가입 요청을 처리하고 메인 페이지로 이동하도록 했지만
  > 메인 페이지 이동 후 다른 페이지로 이동하지 못하는 문제가 발생했습니다.
  >
  > 페이지 요청처럼 응답을 할 경우 현재 url이 /user로 된다는 것을 확인했으며,
  > 페이지를 이동시키기 위해서는 리다이렉션을 사용해야 한다는 것을 알아냈습니다.
  >
  > redirect 헤더를 만들어주는 함수를 만들어 브라우저가 302 응답을 받고,
  > 홈 URL로 페이지 재요청을 하도록 하여 해결했습니다.

---

## 5. 기록

[HTTP GET Method](http/get-method.md)

[HTTP Redirect](http/redirect.md)

[HTTP Status Code](http/status-code.md)

[URI](http/uri.md)

[try with resources](java/try-with-resources.md)

---

## 6. 참고 문서

[REQBIN - GET Method](https://reqbin.com/Article/HttpGet)

[MDN - Redirections in HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP/Redirections)

[Baeldung - Guide to JUnit 5 Parameterized Tests](https://www.baeldung.com/parameterized-tests-junit-5)

[Blog - Junit 5](https://covenant.tistory.com/256)

[Baeldung - A Guide to JUnit 5](https://www.baeldung.com/junit-5)

[Blog - Parameterized Test](https://velog.io/@ohzzi/junit5-parameterizedtest#methodsource)
