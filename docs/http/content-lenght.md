## Content-Length

### 1. 개요

WAS 프로젝트를 진행하던 중 헤더에 Content-Length가 없어도 브라우저에서 문제가 발생하지 않는다는 것을 발견했습니다.

Request Body를 읽기 위해서는 Content-Length를 활용해 정확한 바이트 수만큼 읽어야 하는 반면에, 브라우저는 Content-Length가 없어도 Response Body를 정확하게 읽어냈던 것입니다.

왜 Content-Length가 없어도 문제가 없던 것인지 찾아보고 정리했습니다.

### 2. Content-Length

Content-Length 헤더는 HTTP 응답에서 전송되는 컨텐츠의 길이를 나타내는 헤더입니다.

이 헤더가 없다면, 수신자는 컨텐츠의 길이를 사전에 알 수 없게 됩니다. 그럼에도 불구하고 페이지가 로딩되는 이유는 다음과 같을 수 있습니다.

### 3. Chunked Transfer Encoding

웹 서버가 응답 본문을 전송할 때, Transfer-Encoding 헤더를 사용하여 "chunked" 전송 인코딩을 지원할 수 있습니다. 이 경우에는 응답 본문이 여러 조각으로 나뉘어 전송되며, 브라우저는 이러한 조각들을 조합하여 전체 응답을 복원합니다.

```
HTTP/1.1 200 OK
Content-Type: text/plain
Transfer-Encoding: chunked

7\r\n
Mozilla\r\n
9\r\n
Developer\r\n
7\r\n
Network\r\n
0\r\n
\r\n
```

각 조각은 길이를 나타내는 Content-Length 헤더 없이 전송되며, 브라우저는 컨텐츠의 전체 길이를 미리 알 필요가 없습니다.

하지만 저는 해당 헤더를 사용하지 않았기 때문에 이것이 원인이라고 보기 힘들었습니다.

### 4. Connection close

만약 서버가 응답을 전송한 후에 연결을 닫는다면, 브라우저는 서버로부터 응답을 받고 나서 연결이 닫혔다는 것을 확인하고, 그 이후에 추가 데이터가 없음을 이해합니다.

프로젝트 WAS의 구조는 소켓으로 Connection을 형성하고, Request 요청을 수행하고 Response를 전송한 뒤 Connection을 닫습니다.

이러한 과정을 통해 브라우저는 Connection이 닫혔다는 것을 확인하고, 추가 데이터가 없다는 것을 이해한 뒤 페이지를 불러올 수 있었던 것입니다.

### 5. 결론

Content-Length 헤더가 없어도 브라우저가 알아서 잘 받아 주지만, 브라우저에 따라 결과가 달라질 수 있다는 점을 유의해야 합니다.

## Reference

[MDN - Transfer Encoding](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Transfer-Encoding)