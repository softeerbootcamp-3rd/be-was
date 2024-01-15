# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

---

## 학습 내용

### Request Header

#### Host

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Host

(가상 호스팅을 위해) 서버의 도메인명과 서버가 리스닝하는 (부가적인) TCP 포트를 특정합니다.<br>
포트가 주어지지 않으면, 요청된 서버의 기본 포트(예를 들어, HTTP URL은 "80")를 의미합니다.<br>
Host 헤더의 필드는 모든 HTTP/1.1 요청 메시지 내에 포함되어 전송되어야 합니다.<br>
Host 헤더 필드가 없거나 한 개 이상의 필드를 포함하는 HTTP/1.1 요청 메시지에 대해서는 400 (Bad Request) 상태 코드가 전송됩니다.

#### Connection

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Connection

현재의 전송이 완료된 후 네트워크 접속을 유지할지 말지를 제어합니다.<br>
만약 전송된 값이 keep-alive면, 연결은 지속되고 끊기지 않으며, 동일한 서버에 대한 후속 요청을 수행할 수 있습니다.

#### sec-ch-ua

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-CH-UA

서버가 사용자 에이전트(브라우저), 운영 체제 및 장치에 따라 응답을 다양하게 할 수 있게 해주는 헤더입니다.

#### sec-ch-ua-mobile

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-CH-UA-Mobile

브라우저가 모바일 기기에서 실행되는지 알려주는 헤더입니다.

#### sec-ch-ua-platform

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-CH-UA-Platform

사용자 에이전트가 실행되고있는 플랫폼이나 운영체가 무엇인지 알려주는 헤더입니다.

#### Upgrade-Insecure-Requests

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Upgrade-Insecure-Requests
https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP

암호화와 인증에 대한 클라이언트의 선호를 표현하고 CSP 를 성공적으로 처리할 수 있는지 알려주는 헤더입니다.

CSP : 교차 사이트 스크립팅(XSS)과 데이터 주입 공격을 비롯한 특정 유형의 공격을 탐지하고 완화하는 데 도움이 되는 추가 보안 계층입니다.

#### User-Agent

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent

서버 또는 네트워크 피어가 사용자 에이전트의 애플리케이션, 운영체제, 벤더, 버전을 알게 해주는 문자열입니다.

#### Accept

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept

클라이언트가 이해 가능한 컨텐츠 타입이 무엇인지를 알려주는 헤더이며, MIME 타입으로 표현됩니다.

#### Accept-Encoding

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Encoding

클라이언트가 이해 가능한 컨텐츠 인코딩이 무엇인지를 알려주는 헤더입니다.

#### Accept-Language

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language

클라이언트가 어떤 언어를 이해할 수 있는지, 그리고 지역 설정 중 어떤 것이 더 선호되는지를 알려주는 헤더입니다.

#### Sec-Fetch-Site

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-Site

요청자와 요청 리소스의 site 관계를 알려주는 헤더입니다.

#### Sec-Fetch-Mode

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-Mode

request 의 mode 를 알려주는 헤더입니다.<br>
서버는 이를 통해 HTML 페이지 내의 이동인지, 이미지 또는 다른 리소스 요청인지 구분할 수 있습니다.

#### Sec-Fetch-User

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-User

사용자 행동에 의해 request 가 생성될 경우 포함되는 헤더로, 값이 고정되어 있습니다.

#### Sec-Fetch-Dest

https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Sec-Fetch-Dest

fetch 된 데이터가 어떻게 사용될지 알려주는 헤더입니다.

---

### MIME type

https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type
https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types

Content-Type 헤더는 리소스의 media type을 나타내기 위해 사용됩니다.<br>
여기서 media type 이란 MIME type을 말하는데, 응답에 보내는 파일의 
형식에 맞는 타입으로 지정해주어야 호스트가 정상적으로 정상적으로 사용할 수 있습니다.

---

### Java Thread Model

https://www.baeldung.com/java-threading-models
https://e-una.tistory.com/70

자바 스레드 모델은 매핑 방법에 따라 3 가지가 있습니다.

#### Many-to-One Model

다대일 모델은 여러 사용자 스레드를 하나의 커널 스레드에 매핑하며, 
스레드는 사용자 공간의 스레드 라이브러리에 의해 관리됩니다.

한 스레드가 blocking 되는 경우 전체 프로세스가 block 되며, 한 번에 하나의 스레드만이 커널에 접근할 수 
있기 때문에 멀티 코어 시스템에서 병렬로 실행될 수 없습니다.<br>
하지만 동기화 및 리소스 공유가 쉽기 때문에 실행 시간이 단축된다는 장점이 있습니다.

Java 의 초기 버전의 스레드 모델인 Green Thread 가 이 모델을 사용했으며, 
대부분의 컴퓨터 시스템에서 멀티 코어가 표준이 되면서 그 이점을 살릴 수 없기 때문에 현재는 사용되지 않습니다.

#### One-to-One Model

일대일 모델은 각 사용자 스레드를 각각 하나의 커널 스레드에 매핑합니다. 
따라서 하나의 스레드가 Blocking 되더라도 다른 스레드는 계속 실행되기 때문에 병렬 실행이 용이합니다.

하지만 사용자 스레드를 만들기 위해 커널 스레드를 만들어야 하며, 많은 수의 커널 스레드는 시스템 성능에 부담을 
줄 수 있기 때문에 윈도우나 리눅스는 스레드 수의 제한을 둡니다.

#### Many-to-Many Model

다대다 모델은 여러 개의 사용자 스레드를 그보다 적거나 같은 수의 커널 스레드로 멀티플렉스합니다.<br>
커널 스레드의 수는 응용 프로그램에 따라 다르며, 코어의 수가 많을 수록 많은 커널 스레드를 할당받습니다.

다대다 모델은 다대일 모델과 일대일 모델의 단점을 절충한 방법으로, 필요한 만큼 사용자 스레드를 생성하고 
그에 상응하는 커널 스레드가 병렬로 수행될 수 있습니다.<br>
또한 가장 높은 정확도의 동시성 처리를 제공하는 모델로, 하나의 스레드가 blocking 되었을 경우 커널은 
다른 스레드의 작업을 수행할 수 있습니다.
