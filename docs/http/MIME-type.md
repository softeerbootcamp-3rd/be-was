## MIME (Multipurpose Internet Mail Extensions) type

HTTP MIME Type을 정리하는 문서입니다.

### 1. 개요

Content-Type 헤더는 리소스의 media type을 나타내기 위해 사용됩니다.

여기서 media type 이란 MIME type을 말하는데, 응답에 보내는 파일의
형식에 맞는 타입으로 지정해주어야 호스트가 정상적으로 정상적으로 사용할 수 있습니다.

### 2. 구조 : `type/subtype`

MIME 타입은 가장 일반적으로 슬래시(/)로 구분된 'type'과 'subtype'의 두 부분으로 구성됩니다.

- type : video 또는 text와 같이 데이터 타입이 속하는 일반 카테고리를 나타냅니다.
- subtype : MIME 타입이 나타내는 지정된 타입의 정확한 데이터 종류를 식별합니다.

### 3. 매개변수 : `;parameter=value`

세부정보를 제공하기 위해 선택적 매개변수를 추가할 수 있습니다.

MIME 타입은 대소문자를 구분하지는 않지만 전통적으로 소문자로 쓰여집니다.
매개변수 값은 대소문자를 구분할 수 있습니다.

### 4. 주의점

브라우저는 MIME 타입을 사용하여 URL 처리 방법을 결정합니다.
따라서, 웹 서버가 응답의 Content-Type 헤더에 올바른 MIME 타입을 보내는 것이 중요합니다.

올바르게 구성하지 않으면, 브라우저가 파일 내용을 잘못 해석할 가능성이 높고,
사이트가 제대로 작동하지 않고 다운로드한 파일이 잘못 처리될 수 있습니다.

---

## Reference

[MDN - Header/Content-Type](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type)

[MDN - MIME Types](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types)
