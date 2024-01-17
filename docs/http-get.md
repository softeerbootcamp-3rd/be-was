## GET Method

`GET` 메서드는 리소스를 조회하는 메서드 입니다.

`HTTP` 명세에 의하면 `GET` 요청은 오로지 데이터를 읽을 때만 사용됩니다.
즉, 데이터 변형의 발생하지 않습니다.

또한 GET 요청은 `idempotent`합니다.
따라서 같은 요청을 여러 번 하더라도 변함없이 항상 같은 응답을 받을 수 있습니다.

`POST` 메서드도 사용할 수 있지만, `GET` 메서드는 캐싱이 가능하기 때문에 `GET`을 사용하는 것이 유리합니다.

### 요청 과정

1. 클라이언트에서 GET 요청을 보냅니다.
2. 서버에서는 요청 메세지를 분석해서 내부의 정보를 조회한 뒤 `Response`를 만듭니다.
3. 서버에서 클라이언트로 응답을 해줍니다.

### 정적 데이터 조회

정적 데이터는 쿼리 파라미터 없이 리소스 경로만으로 조회가 가능합니다.

### 동적 데이터 조회

동적 데이터는 리소스 경로와 더불어 쿼리 파라미터가 필요합니다.

- GET /user/create?`userId=id&password=pwd&name=user&email=user@email`
  - 쿼리 파라미터는 GET 요청을 하면서 파라미터를 서버에 보내줄 수 있는 방법입니다.
  - 주소와 쿼리 파라미터는 `?`로 구분됩니다.
  - {key}={value}형태로 작성하며, 파라미터끼리는 `&`를 사용하여 구분합니다.

## Reference

[RFC - GET Method](https://www.rfc-editor.org/rfc/rfc9110.html#name-get)

[REQBIN - GET Method](https://reqbin.com/Article/HttpGet)