## Try with Resources

try-with-resources를 정리하는 문서입니다.

### 1. 개요

try-with-resources는 try-catch-finally의 단점을 보완하기 java7부터 도입된 문법입니다.

### 2. try-catch-finally의 문제점

사용 후에 반납해주어야 하는 자원들은 Closeable 인터페이스를 구현하고 있으며, 사용 후에 close() 메서드를 호출해주어야 했습니다.

Java7 이전에는 close를 호출하기 위해서 try-catch-finally를 이용해서 Null 검사와 함께 직접 호출해야 했습니다.

하지만 이런 방식에는 문제점이 다음과 같습니다.

- 자원 반납에 의해 코드가 복잡해집니다.
- 작업이 번거롭습니다.
- 실수로 자원을 반환하지 못하는 경우가 발생할 수 있습니다.
- 오류 발생시 자원을 반환하지 못하는 경우가 발생할 수 있습니다.
- 에러 스택 트레이스가 누락되어 디버깅이 어렵습니다.

### 3. try-with-resources

AutoCloseable 인터페이스를 구현하고 있는 자원에 대해 적용할 수 있는 문법으로, 자원을 자동으로 반납해주는 문법입니다.

기존 Closeable에 부모 인터페이스로 AutoCloseable을 추가했기 때문에 하위 호환성을 완벽히 지원합니다.

try-with-resources를 사용하면 다음과 같은 이점이 있습니다.
- 코드를 간결하게 만들 수 있습니다.
- 번거로운 자원 반납 작업을 하지 않아도 됩니다.
- 실수로 자원을 반납하지 못하는 경우를 방지할 수 있습니다.
- 에러로 자원을 반납하지 못하는 경우를 방지할 수 있습니다.
- 모든 에러에 대한 스택 트레이스를 남길 수 있습니다.

## Reference

[Baeldung - try-with-resources](https://www.baeldung.com/java-try-with-resources)

[Blog - try-with-resources](https://mangkyu.tistory.com/217)