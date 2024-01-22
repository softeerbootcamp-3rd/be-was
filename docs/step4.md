## 1. 학습 목표

- HTTP POST의 동작방식을 이해하고 이를 이용해 회원가입을 구현할 수 있다.
- HTTP redirection 기능을 이해하고 회원가입 후 페이지 이동에 적용한다.

---

## 2. 기능 요구사항

- 로그인을 GET에서 POST로 수정 후 정상 동작하도록 구현한다.
- 가입을 완료하면 /index.html 페이지로 이동한다.

---

## 3. 학습 내용

---

## 4. 문제 해결

### 1. Request Body 읽기

- 문제
  > request header를 읽었던 방식을 그대로 사용하여 request body를 읽으려고 할 경우 끝없이 대기하는 문제가 발생했습니다.

- 원인
  > buffered Reader의 readline() 함수가 줄을 구분하는 기준은 라인 피드(\n)나 캐리지 리턴(\r) 혹은 캐리지 리턴 다음에 바로 라인피드가 있을 때 (\r\n),
  > 그도 아니라면 EOF에 도달했을 때 입니다.
  > 
  > Request 메시지의 header와 body는 CRLF(캐리지 리턴 라인 피드)으로 구분되기 때문에 
  > readline() 함수로 읽을 수 있었지만 body의 끝에는 CRLF가 없기 때문에 줄의 끝을 찾지 못해
  > 끝없이 대기하게 되는 것입니다.

- 해결
  > Content-Length 헤더 정보를 통해 body의 바이트 수를 알 수 있기 때문에 byte 자료형인 char와
  > Buffered Reader의 read() 함수로 읽었습니다.
  > 

---

## 5. 기록

---

## 6. 참고 문서

[hudi - HTTP Request Message](https://hudi.blog/woowacourse-level4-tcp-troubleshooting/)
