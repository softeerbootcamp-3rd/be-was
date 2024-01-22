# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 1. 학습 목표 
- HTTP Response 에 대해 학습한다.
- MIME 타입에 대해 이해하고 이를 적용할 수 있다.

---

## 2. 기능 요구사항
1. 지금까지 구현한 소스 코드는 stylesheet 와 파비콘 등을 지원하지 못하고 있다. 다양한 컨텐츠 타입을 지원하도록 개선해 본다.
- 지원할 컨텐츠 타입의 확장자 목록: html, css, js, ico, png, jpg

---

## 3. 학습 내용 👨🏻‍💻

##### 1. MIME Type
- MIME(Multipurpose Internet Mail Extensions): 클라이언트에게 전송된 문서에 대해 알려주기 위한 메커니즘으로 다양한 형태의 데이터를 표현하기 위해 사용된다.
  >- 클라이언트에서는 HTTP 헤더에 Accept 헤더 필드를 통해 수용할 수 있는 MIME 타입을 전송한다.
  >- MIME 타입은 HTTP 헤더에 Content-Type 헤더 필드를 통해 전송된다.
  >- MIME 타입은 type/subtype 으로 구성되며, type은 대분류, subtype은 소분류를 의미한다.

- MIME 타입의 종류
  >- text/html, text/css, text/javascript, image/png, image/jpeg, image/gif, application/json, application/xml 등

- MIME 타입 설정의 중요성
  >- 브라우저는 서버로부터 전송받은 리소스의 MIME 타입을 통해 리소스를 어떻게 처리할지 결정한다. <Br>
    따라서, 서버가 적절한 MIME 타입을 설정하지 않으면 브라우저는 전송한 컨텐츠에 대해 기본적인 동작을 허용하지 않을 수 있다.


##### 2. Interface VS Abstract Class
- Interface
  >- Interface를 구현하는 Class들에 대해 동일한 method를 Overriding하여 여러가지 형태로 구현할 수 있도록 다형성을 높여준다.
  >- Overriding을 통해 다형성을 높여주기 때문에 Interface를 통해 구현된 Class들은 서로 다른 형태로 동작할 수 있다. <br>
  다른 의미로, **메소드의 구현을 강제**하기 위해 존재한다.
- Abstract Class
  >- Abstract Class를 상속받으며 같은 부모 Class를 상속받는 자식 Class들 간에 공통 기능을 각각 구현할 수 있도록 확장성을 높여준다. <br>
  즉, **Subclass들에게 공통된 필드와 메소드를 제공**하기 위해 존재한다.
  >- 다중 상속이 불가능한 이유: 다중 상속을 허용하면, 서로 다른 부모 Class로부터 상속받은 method들이 동일한 method 시그니처를 갖는 경우가 발생할 수 있다. (다이아몬드 문제)
  
- 사전 지식 정리
  >- 다형성(Polymorphism): 하나의 method나 Class가 있을 때 이것들이 다양한 방법으로 동작하는 것을 의미한다.
  >- Overriding: 부모 Class의 method를 자식 Class에서 재정의하는 것으로, 다형성을 높여준다.
  >- Overloading: 같은 Class 내에서 method의 이름은 같지만, 매개변수의 타입, 개수, 순서가 다른 여러 개의 method를 선언하는 것을 의미한다. <br>
  마찬가지로 다형성을 높여준다.


##### 3. DispatcherServlet
- 스프링부트에서는 DispatcherServlet을 통해 클라이언트의 요청을 받고, 요청에 대한 응답을 보내는 역할을 한다. <Br>
  최종 프로젝트에서는 스프링부트를 활용한 웹 애플리케이션을 구현할 것이기 때문에, 자바로 WAS를 구현하는 과정에서 DispatcherServlet의 동작 원리를 학습하고 흐름을 직접 작성해봄으로써 높은 이해도를 가져갈 수 있을 것이라고 생각했다.
- DispatcherServlet의 동작 과정
  <img width="947" alt="스크린샷 2024-01-21 오후 11 58 21" src="https://github.com/taegon98/HMG-Softeer-be-was/assets/102223636/781d1df0-a51c-40b6-b922-e84d972538e8">
  >1. 클라이언트의 요청이 들어오면 DispatcherServlet(Front Controller)이 요청을 받는다.
  >2. DispatcherServlet은 HandlerMapping을 통해 요청에 대한 Controller를 찾는다.
  >3. DispatcherServlet은 HandlerAdapter를 통해 Controller 요청을 위임한다.
  >4. HandlerAdapter는 Controller에게 요청을 위임한다.
  >5. Controller는 비지니스 로직을 수행하고, 결과를 HandlerAdapter에게 반환한다.
  >6. HandlerAdapter는 Controller의 결과를 DispatcherServlet에게 반환한다.
  >- HandlerAdapter의 ReturnTypeHandler를 통해 Controller의 결과를 적절한 형태로 변환 후 DispatcherServlet에게 반환한다.
  >- 컨트롤러가 ResponseEntity를 반환하면 HttpEntityMethodProcessor가 응답 객체를 직렬화하고 HttpStatus를 설정한다. 만약 컨트롤러가 View 이름을 반환한다.
  >7. DispatcherServlet은 ViewResolver를 통해 View의 이름에 맞는 View를 찾아서 반환한다.
  >8. DispatcherServlet은 서버의 응답(응답 데이터 or View)을 클라이언트에게 전송한다.

- HandlerAdapter가 필요한 이유: HandlerAdapter는 Spring MVC에서 컨트롤러의 실행 결과를 처리하고 적절한 응답 형태로 변환하여 DispatcherServlet에게 전달하는 인터페이스 역할을 한다.
  >1. 컨트롤러의 다양한 **반환 유형을 표준화된 형태로 변환**하여 전달: ModelAndView를 반환하는 경우. 문자열을 반환하여 뷰의 이름을 나타내는 경우, ResponseEntity를 반환하여 HTTP 응답을 나타내는 경우 등 컨트롤러의 실행 결과는 다양한 형태로 반환될 수 있다.
  >2. 컨트롤러 실행 **전/후에 추가적인 처리**를 수행하는 표준화된 인터페이스 제공: @RequestParam, @RequestBody 등을 처리하기 위한 ArgumentResolver, 응답 시에 ResponseEntity의 Body를 Json으로 직렬화하는 등의 처리를 하는 ReturnValueHandler 등이 핸들러 어댑터에서 처리된다.

---

## 4. 추가로 학습할 내용 👨🏻‍💻
- Step-1, Step-2에서 당글님으로부터 받았던 공통적인 피드백이 네이밍이 명료하지 않다는 것이었다. <br>
  변수명, 메소드명만 보고도 어떤 역할을 하는지 알 수 있도록 리팩토링 과정을 진행했다. <br>
  이 과정에서 네이밍에 대한 고민을 많이 했고, 해당 내용을 정리해보고자 한다.
  >- 학습 내용 정리: https://velog.io/@taegon1998/%EC%A2%8B%EC%9D%80-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%9C%84%ED%95%9C-%EB%84%A4%EC%9D%B4%EB%B0%8D

---

## 5. 1주차를 마치며 👋🏻
- 아쉬웠던 것
  >1. **네이밍**: 개발 업무는 혼자가 아닌 팀으로 진행되기 때문에, 협업을 위해서는 명료한 네이밍이 필수라고 생각한다. <br>
  다음 미션부터는 네이밍에 더 많은 시간을 할애하고, 피어들로부터 피드백을 받아보고자 한다.
  >2. **Commit Message**: 네이밍과 마찬가지로, Commit Message만 보고도 어떤 변경사항 또는 기능 추가가 이루어졌는지 알 수 있어야 한다.
  첫 줄에 무엇을 했는지, 두 번째 줄은 띄우고, 세 번째 줄부터 구체적인 내용을 작성하는 습관을 **꼭!!** 들이고자 한다.
  

- 좋았던 것
  >1. **고민하기**: 내가 작성한 코드를 팀원들에게 설명해주면서 내 코드에 대한 Why를 생각해보게 되었다. <br>
  당연하게 생각하고 작성했던 코드에 대한 이유를 생각해보는 과정에서 학습할 것이 엄청나게 많이 생겨났고, 이를 정리하면서 Why에 대한 고민을 어느정도 해결해볼 수 있었다.
  >2. **우리팀**: 배울게 정말 많았던 우리팀 팀원들과 함께 공부할 수 있어서 1주일이라는 짧은 시간동안 많은 것을 배울 수 있었다. <br>
  서로 좋은점은 물어보고 내것으로 만들기도 하고, 모두가 모르는 것은 피어 세션을 통해 함께 공부하기도 하면서 의미 그대로 **함께** 성장한 것 같아서 무척이나 뿌듯하다.
  한 주 동안 고생한 BE-7조 팀원들 성환, 수지, 주환 모두 고생 많았고 고맙다! 😊

- 다음 Mission에서 도전해볼 것 🤔
  >- TDD 방식으로 개발해보기: 이번 1주차 미션에서는 구현과 비지니스 로직 구현에 집중했기 때문에, TDD 방식으로 개발해보지 못했다. <br>
  이에 따라, Test Code를 작성하면서 원본 코드를 수정하는 상황이 발생했다.
  많은 훌륭하신 개발자 분들이 테스트가 용이한 코드가 좋은 코드라고 말씀하셨기 때문에, 이번 미션에서는 TDD 방식으로 개발해보고 실제로 어떤 장점이 있는지 경험해보고자 한다.
