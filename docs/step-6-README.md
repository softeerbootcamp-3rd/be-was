# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was
를 참고하여 작성되었습니다.

## 1. 학습 목표
- 세션 정보를 바탕으로 주어인 요청에 대해 동적인 HTML을 응답하도록 구현할 수 있다.

---

## 2. 기능 요구사항
1. 사용자가 로그인 상태일 경우 /index.html에서 사용자 이름을 표시해 준다.

2. 사용자가 로그인 상태가 아닐 경우 /index.html에서 [로그인] 버튼을 표시해 준다.

3. 사용자가 로그인 상태일 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.

4. http://localhost:8080/user/list  페이지 접근시 로그인하지 않은 상태일 경우 로그인 페이지(login.html)로 이동한다.

---

## 3. 학습 내용 👨🏻‍💻

##### 1. Cookie attribute
- Set-Cookie: HTTP 표준(RFC 6265)에 따라, HTTP 응답에서 여러 개의 쿠키를 설정할 때는 각 쿠키마다 별도의 Set-Cookie를 사용해야 한다.
  >- step-6 구현 과정에서는 아래와 같이 쿠키를 설정하였다.
  >   - `Set-Cookie: sid=1234; Path=/; Max-Age=120`: 해당 쿠키를 사용하여 세션을 구현
  >   - `Set-Cookie: user-name={ }; Path=/; Max-Age=120`: 해당 쿠키를 사용하여 사용자 이름을 표시하도록 구현
- Max-Age: 쿠키의 유효기간을 설정할 수 있는 속성
  >- Max-Age가 양수이면 해당 시간이 지나면 쿠키를 삭제: 해당 기능을 사용하여 자동 로그인 구현 가능
  >- Max-Age가 0이면 쿠키를 삭제: 해당 기능을 사용하여 로그아웃 구현
- Path: HTTP 표준(RFC 6265)에 따라, Path 속성을 통해 쿠키의 범위를 제한할 수 있다.
  >- 쿠키는 설정된 Path 속성에 지정된 경로나 그 하위 경로에 대한 요청에서만 서버로 전송된다.
  >- 쿠키의 범위를 특정 서브디렉토리 또는 웹 애플리케이션의 특정 부분으로 제한하는 데 사용될 수 있다.
  >- step-6 구현 과정에서는 Path=/로 설정하여 모든 요청에 대해 쿠키를 전송하도록 하였다.

##### 2. StringBulider
- 자바에서 문자열을 효율적으로 조작하기 위해 사용되는 클래스
- 가변(mutable)
  >- String은 불변(immutable)이기 때문에 문자열을 변경할 때마다 새로운 객체를 생성한다.
  >- StringBuilder는 가변(mutable)이기 때문에 기존 객체를 변경한다.
  >   - 객체를 생성하지 않고 문자열을 변경하기 때문에 성능이 뛰어나다.
  >   - 큰 데이터를 다루거나 반복문 내에서 문자열을 조작할 때 성능 차이가 크다.

- 메서드 지원
  >- append(), insert(), delete(), replace() 등 문자열을 조작하기 위한 다양한 메서드를 제공한다.
  >- String은 immutable이기 때문에 문자열을 조작하기 위한 메서드를 제공하지 않는다.

- 쓰레드 안전성
  >- StringBuilder는 쓰레드 안전성을 보장하지 않는다.
  >- StringBuffer는 쓰레드 안전성을 보장한다.
  >  - StringBuffer 내부에서 synchronized 키워드를 사용하여 쓰레드 안전성을 보장하지만, StringBuilder는 안정성을 포기하고 성능을 높였다.
  >- **결론**: 단일 쓰레드 or 문자열 조작이 빈번하게 일어나지 않는 경우 StringBuilder를 사용 / 멀티 쓰레드 or 문자열 조작이 빈번하게 일어나는 경우 StringBuffer를 사용하는 것이 스레드 안전하다.

- Java Platform Standard Edition API Specification - StringBuilder
  >- "A mutable sequence of characters. This class provides an API compatible with StringBuffer, but with no guarantee of synchronization. This class is designed for use as a drop-in replacement for StringBuffer in places where the string buffer was being used by a single thread (as is generally the case). Where possible, it is recommended that this class be used in preference to StringBuffer as it will be faster under most implementations."

##### 3. StringBuilder class의 replace() 메서드
- replace() 메서드: start 인덱스부터 시작하여 end 인덱스 전까지의 문자열을 str로 교체한다.
해당 과정에서 원래의 문자열은 변경되며, 새로운 객체가 생성되지 않는다.
  >- start: 교체를 시작할 인덱스
  >- end: 교체를 종료할 인덱스
  >- str: start와 end 사이에 삽입될 문자열
  >- **replace(int start, int end, String str)**: Replaces the characters in a substring of this sequence with characters in the specified String.

---

## 4. Trouble Shooting 🚀
- Logout 기능 구현 시, 세션이 유지되는 문제
  >- 원인: 브라우저 쿠키에 저장된 세션 정보가 삭제되지 않았기 때문
  >- 해결 방법: 로그아웃 버튼 클릭 시, Max-Age를 0으로 설정하여 브라우저 쿠키에 저장된 세션 정보를 삭제하도록 구현

- 창 두 개를 열고 테스트를 진행할 때, 세션을 공유하는 문제
  >- 원인: 브라우저 쿠키에 저장된 세션 정보가 동일하기 때문
  >- 해결 방법: 크롬 브라우저의 경우, 시크릿 모드를 사용할 수 있다.
  >  - 시크릿 모드: 기존 브라우저 창과 독립된 쿠키와 세션을 가며, 방문 기록, 쿠키, 사이트 데이터가 브라우저를 닫을 때 삭제된다.

----

## 5. 2주차를 마치며 👋🏻
- 아쉬웠던 것
  >1. **중복 코드**: 가독성을 높이기 위해 의미별로 메서드를 분리하면서 코드를 작성했던 것 같다. <br>
  중복되는 코드가 있다는 피드백을 받고 전체 코드를 읽어가며 중복되는 코드를 하나의 메서드로 합쳤지만, 메서드의 길이와 조건에 따른 분기가 많아져 가독성이 떨어지는 것 같다는 느낌을 받았다.
  가독성과 중복 코드를 줄이는 것 사이에서 합의점을 찾기 위해 설계 과정을 더욱 깊게 고민해보고자 한다.
  >2. **코드 리뷰**: 다른 동료들의 코드를 읽는 것이 아직까지 어색하다.<br>
  코드 리딩 스킬을 개선하기 위해서, 이번주에는 많은 동기들에게 코드 설명을 요청하고 질문하고 피드백도 주고받으려고 노력했다.
  아직 많이 부족하지만 이번주도 열심히 물어보고 다닐 것이다.

- 좋았던 것
  >1. **TDD**:: 말로만 듣던 TDD 방식으로 개발해보았다. <br>
  완벽한 TDD 방식은 아니지만, 시나리오를 먼저 생각하고 대부분의 테스트 코드를 먼저 작성한 후에 테스트 코드를 통과하기 위한 코드를 작성하였다. <br>
  가장 큰 장점은 테스트 코드를 작성하면서 구현해야 할 기능에 대해 명확하게 정리할 수 있었다는 것이었다. 즉, 리소스 낭비를 줄일 수 있었다.
  TDD 이외에도, DDD, BDD 방식 등을 학습해봐서 나에게 또는 팀에게 적합한 방식을 찾아보고자 한다.
  >2. **Commit Message**: 첫 줄에는 무엇을 했는지, 두 번째 줄은 띄우고, 세 번째 줄부터 구체적인 내용을 **모든 커밋에 대해** 작성하였다. <br>
  커밋 메세지를 생각하고 작성하는 것에 정말 많은 시간을 할애했지만, 그만큼 커밋 메세지를 통해 어떤 기능을 구현했는지, 어떤 코드를 변경했는지 알 수 있었다.
  최종 프로젝트 과정에서도 해당 규칙만큼은 꼭 팀원들과 함께 지키고 싶다.

- 다음 Mission에서 도전해볼 것 🤔
  >- step-5, step-6를 진행하면서 공식 문서 또는 MDN 문서를 읽으면서 학습을 진행했다. <br>
  평소에 Tistory 또는 velog와 같은 기술블로그에서만 학습을 진행했는데, 공식 문서를 읽으면서 학습을 진행하니까 이해하는 과정에 대한 시간은 더욱 길어졌다. <br>
  하지만 신뢰할 수 있는 정보를 얻을 수 있었고, 짧은 기간이였지만 학습 속도도 조금은 더 빨라진 것 같다. <br>
  훌륭한 기술블로그, 참고하면 좋은 글들도 너무 많지만, 이것들에 더해 공식 문서를 읽어보는 습관을 들이고자 한다.
