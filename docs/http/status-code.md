## Status Code

HTTP Status Code를 정리하는 문서입니다.

### 1. 개요

HTTP 응답 상태 코드는 특정 HTTP 요청이 성공적으로 완료되었는지 알려줍니다.

5개의 그룹으로 나누어지며, 정보를 제공하는 응답, 성공 응답, 리다이렉트, 클라이언트 에러, 서버 에러가 있습니다.

상태 코드는 IANA(Internet Assigned Numbers Authority)라는 인터넷 할당 번호 관리기관이 HTTP 상태 코드 레지스트리라는 이름으로 관리하고 있습니다.

### 2. 1XX : Informational (정보제공)

임시 응답으로 현재 클라이언트의 요청까지는 처리되었으니 계속 진행하라는 의미입니다.

### 3. 2XX : Success (성공)

클라이언트의 요청이 서버에서 성공적으로 처리되었다는 의미입니다.

- 200 : OK
  > 서버가 요청을 성공적으로 처리하였다는 의미입니다.

### 4. 3XX : Redirection (리다이렉션)

완전한 처리를 위해서 추가 동작이 필요한 경우입니다.

주로 서버의 주소 또는 요청한 URI의 웹 문서가 이동되었으니 그 주소로 다시 시도하라는 의미입니다.

- 302 : Found
  > 요청한 리소스가 Location 헤더에 지정된 URL로 일시적으로 이동되었음을 나타냅니다. 
  > 
  > 브라우저는 이 페이지로 리디렉션되지만 검색 엔진은 리소스에 대한 링크를 업데이트하지 않습니다.

- 307 : Temporary Redirect
  > 요청한 리소스가 Location 헤더에 주어진 URL 로 임시로 옮겨졌다는 것을 나타냅니다.
  > 
  > 302와 다르게 원래 요청한 메소드와 Body 를 재사용하여 요청을 리다이렉트 합니다.

### 5. 4XX : Client Error (클라이언트 에러)

없는 페이지를 요청하는 등 클라이언트의 요청 메시지 내용이 잘못된 경우를 의미합니다.

- 400 : Bad Request
  > 요청의 구문이 잘못되었다는 의미입니다.
  > 
  > 클라이언트가 모르는 4xx 계열 응답 코드가 반환된 경우에도 클라이언트는 400과 동일하게 처리하도록 규정하고 있습니다.

- 404 : Not Found
  > 지정한 리소스를 찾을 수 없다는 의미입니다.

### 6. 5XX : Server Error (서버 에러)

서버 사정으로 메시지 처리에 문제가 발생한 경우입니다.

서버의 부하, DB 처리 과정 오류, 서버에서 예외가 발생하는 경우를 의미합니다.

## Reference

[MDN - HTTP Status](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

[IANA - Status Code](https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml)

[RFC - Status Code](https://datatracker.ietf.org/doc/html/rfc2616#section-10)