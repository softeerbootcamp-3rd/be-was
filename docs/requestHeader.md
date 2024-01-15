### Host

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Host

(가상 호스팅을 위해) 서버의 도메인명과 서버가 리스닝하는 (부가적인) TCP 포트를 특정합니다.<br>
포트가 주어지지 않으면, 요청된 서버의 기본 포트(예를 들어, HTTP URL은 "80")를 의미합니다.<br>
Host 헤더의 필드는 모든 HTTP/1.1 요청 메시지 내에 포함되어 전송되어야 합니다.<br>
Host 헤더 필드가 없거나 한 개 이상의 필드를 포함하는 HTTP/1.1 요청 메시지에 대해서는 400 (Bad Request) 상태 코드가 전송됩니다.

### Connection

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Connection

현재의 전송이 완료된 후 네트워크 접속을 유지할지 말지를 제어합니다.<br>
만약 전송된 값이 keep-alive면, 연결은 지속되고 끊기지 않으며, 동일한 서버에 대한 후속 요청을 수행할 수 있습니다.

### sec-ch-ua

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-CH-UA

서버가 사용자 에이전트(브라우저), 운영 체제 및 장치에 따라 응답을 다양하게 할 수 있게 해주는 헤더입니다.

### sec-ch-ua-mobile

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-CH-UA-Mobile

브라우저가 모바일 기기에서 실행되는지 알려주는 헤더입니다.

### sec-ch-ua-platform

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-CH-UA-Platform

사용자 에이전트가 실행되고있는 플랫폼이나 운영체가 무엇인지 알려주는 헤더입니다.

### Upgrade-Insecure-Requests

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Upgrade-Insecure-Requests
https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP

암호화와 인증에 대한 클라이언트의 선호를 표현하고 CSP 를 성공적으로 처리할 수 있는지 알려주는 헤더입니다.

CSP : 교차 사이트 스크립팅(XSS)과 데이터 주입 공격을 비롯한 특정 유형의 공격을 탐지하고 완화하는 데 도움이 되는 추가 보안 계층입니다.

### User-Agent

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent

서버 또는 네트워크 피어가 사용자 에이전트의 애플리케이션, 운영체제, 벤더, 버전을 알게 해주는 문자열입니다.

### Accept

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept

클라이언트가 이해 가능한 컨텐츠 타입이 무엇인지를 알려주는 헤더이며, MIME 타입으로 표현됩니다.

### Accept-Encoding

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Encoding

클라이언트가 이해 가능한 컨텐츠 인코딩이 무엇인지를 알려주는 헤더입니다.

### Accept-Language

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language

클라이언트가 어떤 언어를 이해할 수 있는지, 그리고 지역 설정 중 어떤 것이 더 선호되는지를 알려주는 헤더입니다.

### Sec-Fetch-Site

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-Site

요청자와 요청 리소스의 site 관계를 알려주는 헤더입니다.

### Sec-Fetch-Mode

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-Mode

request 의 mode 를 알려주는 헤더입니다.<br>
서버는 이를 통해 HTML 페이지 내의 이동인지, 이미지 또는 다른 리소스 요청인지 구분할 수 있습니다.

### Sec-Fetch-User

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-User

사용자 행동에 의해 request 가 생성될 경우 포함되는 헤더로, 값이 고정되어 있습니다.

### Sec-Fetch-Dest

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-Dest

fetch 된 데이터가 어떻게 사용될지 알려주는 헤더입니다.