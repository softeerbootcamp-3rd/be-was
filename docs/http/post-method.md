## POST Method

HTTP POST Method를 정리하는 문서입니다.

### 1. 개요

서버로 데이터를 전송하는 메서드이며, 요청 본문의 유형은 Content-Type 헤더로 나타냅니다.

### 2. Content-Type

- `application/x-www-form-urlencoded`
  > "&" 기호로 분리되고, "=" 기호로 값과 키를 연결하는 key-value tuple로 인코딩되는 값이며,
  > 알파벳이 아닌 문자들은 퍼센트 인코딩됩니다.

- `multipart/form-data`
  > 각 값은 데이터 블록(body)으로 전송되며 사용자 에이전트에서 정의한 구분자가 각 부분을 구분합니다.
  > 키는 각 부분의 Content-Disposition 헤더에 제공됩니다.

- `text/plain`
  > 텍스트 형식입니다.

### 3. Idempotent (멱등성)

멱등성이란 동일한 요청을 한 번 보내는 것과 여러 번 연속으로 보내는 것이 같은 효과를 지니고, 서버의 상태도 동일하게 남는 속성입니다.

POST 요청은 데이터를 서버로 보낼때 주로 사용되며, 여러 번 호출될 경우 데이터가 중복되어 생성됩니다.

따라서 GET 요청과 달리 POST 요청은 멱등성을 가지지 않습니다.

## Reference

[MDN - POST](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST)
