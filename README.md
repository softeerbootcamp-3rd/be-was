# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.


# 학습 내용
## HTTP 요청
### HTTP란?
Hyper Text Transfer Protocol의 약자다.
즉, 하이퍼텍스트 문서를 교환하기 위해 만들어진 Protocol로서 웹 통신에서 형식을 정해놓은 약속이다.

### 요청과 응답
클라이언트가 서버에 HTTP Request를 보내고, 서버가 HTTP Response를 돌려보내는 식이다.

#### Stateless
요청과 응답에 대한 정보가 저장되지 않기 때문에, 다시 요청을 보낼 때 이전 대화에 대해 알지 못한다.

### HTTP Request의 구조
1. Starter line
action의 정보를 담는다. 또한, 어디로 요청할지를 담는다.
2. Headers
요청에 대한 추가 정보를 담는다.
Key: Value로 구성된다.
Target과 Host를 합쳐서 인식힌다.(주소를?)
3. Body
데이터가 담긴다.
예를 들어, 로그인의 경우 데이터를 보내야하기 때문에 body를 사용한다.
어떤 사항을 요청하는지는 Content-type에 의해 결정된다. <- 개발자 도구의 Network 패널에서 확인 가능하다.

### HTTP Response의 구조
1. Status Line
- 응답의 상태를 간략하게 나타낸다.
- HTTP version, Status code, Status text로 구성된다.
2. Headers
- 요청의 header와 동일하다.
- 응답에서만 사용되는 header값들이 있다. (예외)
3. Body
- 요청의 header와 동일하다.
- 항상 데이터가 들어있지는 않으며, 필요한 경우에만 사용된다.

### HTTP Method
HTTP 요청이 원하는 action
#### GET
- 서버로부터 데이터를 받아올 때 사용
#### POST
- 서버로 데이터를 올릴 때 사용
- 데이터를 생성/수정/삭제 할 때 주로 사용
#### PUT
- 데이터를 생성할 때 사용
#### DELETE
- 데이터를 서버에서 삭제할 때 사용